<template>
  <div class="component-upload-image">
    <el-upload
      multiple
      :disabled="disabled"
      :action="uploadImgUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :data="data"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      ref="imageUpload"
      :on-remove="handleDelete"
      :show-file-list="true"
      :headers="headers"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{ hide: fileList.length >= limit }"
    >
      <el-icon style="font-size: 50px;">
        <i-ep-plus />
      </el-icon>
    </el-upload>

    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip && !disabled">
      请上传
      <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b> </template>
      <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b> </template>
      的文件
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="预览"
      width="800"
      append-to-body
    >
      <img
        :src="dialogImageUrl"
        style="display: block; max-width: 100%; margin: 0 auto"
      />
    </el-dialog>
  </div>
</template>

<script setup name="ImageUpload">
import { ref, reactive, watch, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import Sortable from 'sortablejs'
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/authStore'

// 定义 props
const props = defineProps({
  modelValue: {
    type: [String, Object, Array],
    default: null
  },
  action: {
    type: String,
    default: null
  },
  data: {
    type: Object
  },
  limit: {
    type: Number,
    default: 5
  },
  fileSize: {
    type: Number,
    default: 5
  },
  fileType: {
    type: Array,
    default: () => ["png", "jpg", "jpeg"]
  },
  isShowTip: {
    type: Boolean,
    default: true
  },
  disabled: {
    type: Boolean,
    default: false
  },
  drag: {
    type: Boolean,
    default: true
  },
  isLocal: {
    type: Boolean,
    default: true
  },
})

// 定义 emits
const emit = defineEmits(['update:modelValue'])

// 响应式变量
const number = ref(0)
const uploadList = ref([])
const dialogImageUrl = ref("")
const dialogVisible = ref(false)
const baseUrl = ref("")
const fileList = ref([])
const imageUpload = ref(null) // 图片上传组件引用

// 认证处理
const authStore = useAuthStore()
const { accessToken } = storeToRefs(authStore)

// 请求头
const headers = computed(() => ({
  'X-Access-Token': accessToken.value ? `Bearer ${accessToken.value}` : "false accessToken"
}))

// 计算上传地址
const uploadImgUrl = computed(() => {
  const defaultAction = props.isLocal ? "/localStorage/upload" : "/cloudStorage/upload"
  const action = props.action || defaultAction
  return import.meta.env.VITE_APP_BASE_API + action
})

// 是否显示提示
const showTip = computed(() => props.isShowTip && (props.fileType || props.fileSize))

// 初始化基础URL
const initBaseUrl = () => {
  baseUrl.value = props.isLocal
    ? import.meta.env.VITE_APP_BASE_API
    : (import.meta.env.VITE_APP_CLOUD_PIC_URL || '') + "/"
}
initBaseUrl()

// 监听 isLocal 变化
watch(() => props.isLocal, initBaseUrl)

// 监听 modelValue 变化
watch(() => props.modelValue, (val) => {
  if (val) {
    const list = Array.isArray(val) ? val : val.split(',')
    fileList.value = list.map(item => {
      if (typeof item === "string") {
        if (!isExternal(item) && !item.includes(baseUrl.value)) {
          return { name: item, url: baseUrl.value + item }
        }
        return { name: item, url: item }
      }
      return item
    })
  } else {
    fileList.value = []
  }
}, { deep: true, immediate: true })

// 判断是否外部链接
const isExternal = (url) => {
  return /^(https?:|mailto:|tel:)/.test(url)
}

// 上传前验证
const handleBeforeUpload = (file) => {
  // 验证文件格式
  if (props.fileType.length) {
    const extension = file.name.split('.').pop()?.toLowerCase() || ''
    const isValidExtension = props.fileType.includes(extension)

    if (!isValidExtension) {
      ElMessage.error(`文件格式不正确，请上传${props.fileType.join("/")}图片格式文件!`)
      return false
    }
  }

  // 验证文件名
  if (file.name.includes(',')) {
    ElMessage.error('文件名不能包含英文逗号!')
    return false
  }

  // 验证文件大小
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize
    if (!isLt) {
      ElMessage.error(`上传图片大小不能超过 ${props.fileSize} MB!`)
      return false
    }
  }

  number.value++
  return true
}

// 处理文件超出限制
const handleExceed = () => {
  ElMessage.error(`上传文件数量不能超过 ${props.limit} 个!`)
}

// 上传成功处理
const handleUploadSuccess = (res, file) => {
  if (res.code === 200) {
    uploadList.value.push({ name: res.fileName, url: res.fileName })
    uploadedSuccessfully()
  } else {
    number.value--
    ElMessage.error(res.msg || "上传失败")
    handleDelete(file)
    uploadedSuccessfully()
  }
}

// 删除文件
const handleDelete = (file) => {
  const fileIndex = fileList.value.findIndex(f => f.name === file.name)
  if (fileIndex > -1) {
    fileList.value.splice(fileIndex, 1)
    emit("update:modelValue", listToString(fileList.value))
  }
}

// 上传错误处理
const handleUploadError = () => {
  ElMessage.error("上传图片失败，请重试")
  number.value--
}

// 上传完成处理
const uploadedSuccessfully = () => {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = [...fileList.value, ...uploadList.value]
    uploadList.value = []
    number.value = 0
    emit("update:modelValue", listToString(fileList.value))
  }
}

// 图片预览
const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

// 文件列表转字符串
const listToString = (list, separator = ",") => {
  return list
    .map(item => item.url?.replace(baseUrl.value, "") || "")
    .filter(url => url !== "")
    .join(separator)
}

// 初始化拖拽排序
onMounted(() => {
  // if (props.drag && !props.disabled) {
  //   nextTick(() => {
  //     const uploadElement = imageUpload.value?.$el?.querySelector('.el-upload-list')
  //     if (uploadElement) {
  //       Sortable.create(uploadElement, {
  //         onEnd: (evt) => {
  //           const movedItem = fileList.value.splice(evt.oldIndex, 1)[0]
  //           fileList.value.splice(evt.newIndex, 0, movedItem)
  //           emit("update:modelValue", listToString(fileList.value))
  //         }
  //       })
  //     }
  //   })
  // }
})
</script>

<style scoped lang="scss">
.component-upload-image {
  // 隐藏按钮
  :deep(.hide .el-upload--picture-card) {
    display: none;
  }

  // 禁用状态隐藏按钮
  :deep(.el-upload-list--picture-card.is-disabled + .el-upload--picture-card) {
    display: none !important;
  }

  // 优化动画性能
  :deep(.el-list-enter-active),
  :deep(.el-list-leave-active) {
    transition: all 0.3s ease;
  }

  :deep(.el-list-enter-from),
  :deep(.el-list-leave-to) {
    opacity: 0;
    transform: translateY(10px);
  }
}
</style>
