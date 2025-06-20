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

<script>
import { defineComponent, ref, reactive, watch, computed, onMounted, nextTick } from 'vue'
import { isExternal } from "@/utils/validate"
import { ElMessage } from 'element-plus'
import Sortable from 'sortablejs'
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/authStore'; // 导入认证 store

export default defineComponent({
  name: "ImageUpload",
  props: {
    modelValue: [String, Object, Array],
    // 上传接口地址
    action: {
      type: String,
      default: null
    },
    // 上传携带的参数
    data: {
      type: Object
    },
    // 图片数量限制
    limit: {
      type: Number,
      default: 5
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 5
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ["png", "jpg", "jpeg"]
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    // 禁用组件（仅查看图片）
    disabled: {
      type: Boolean,
      default: false
    },
    // 拖动排序
    drag: {
      type: Boolean,
      default: true
    },
    //是否本地（true：本地，false：云）
    isLocal: {
      type: Boolean,
      default: true
    },
  },
  emits: ['update:modelValue'],
  setup(props, { emit, refs }) {
    const number = ref(0)
    const uploadList = ref([])
    const dialogImageUrl = ref("")
    const dialogVisible = ref(false)
    const baseUrl = ref("")
    const fileList = ref([])

    //请求头添加token进行请求
    const authStore = useAuthStore(); // 初始化 store
    const { accessToken } = storeToRefs(authStore)
    const headers = reactive({
      'X-Access-Token': accessToken ? `Bearer ${accessToken.value}` : "false accessToken",
    })

    // 计算上传的图片服务器地址
    const uploadImgUrl = computed(() => {
      const defaultAction = props.isLocal ? "/common/upload" : "/cloudStorage/upload";
      const action = props.action || defaultAction;
      return import.meta.env.VITE_APP_BASE_API + action;
    })

    // 是否显示提示
    const showTip = computed(() => {
      return props.isShowTip && (props.fileType || props.fileSize)
    })

    // 初始化baseUrl
    const initBaseUrl = () => {
      baseUrl.value = props.isLocal
        ? import.meta.env.VITE_APP_BASE_API
        : (import.meta.env.VITE_APP_CLOUD_PIC_URL || '') + "/"
    }
    initBaseUrl()

    // 监听props变化
    watch(() => props.isLocal, () => {
      initBaseUrl()
    })

    // 监听modelValue变化
    watch(() => props.modelValue, (val) => {
      if (val) {
        // 首先将值转为数组
        const list = Array.isArray(val) ? val : val.split(',')
        // 将数组转为对象数组
        fileList.value = list.map(item => {
          if (typeof item === "string") {
            // 处理相对路径的情况
            if (item.indexOf(baseUrl.value) === -1 && !isExternal(item)) {
              return {
                name: item,
                url: baseUrl.value + item
              }
            } else {
              // 外部链接或完整路径
              return {
                name: item,
                url: item
              }
            }
          }
          return item
        })
      } else {
        fileList.value = [] // 无值时清空
      }
    }, { deep: true, immediate: true })

    // 上传前loading加载
    const handleBeforeUpload = (file) => {
      let isImg = false
      if (props.fileType.length) {
        let fileExtension = ""
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1)
        }
        isImg = props.fileType.some(type => {
          if (file.type.indexOf(type) > -1) return true
          if (fileExtension && fileExtension.indexOf(type) > -1) return true
          return false
        })
      } else {
        isImg = file.type.indexOf("image") > -1
      }

      if (!isImg) {
        ElMessage.error(`文件格式不正确，请上传${props.fileType.join("/")}图片格式文件!`)
        return false
      }
      if (file.name.includes(',')) {
        ElMessage.error('文件名不正确，不能包含英文逗号!')
        return false
      }
      if (props.fileSize) {
        const isLt = file.size / 1024 / 1024 < props.fileSize
        if (!isLt) {
          ElMessage.error(`上传头像图片大小不能超过 ${props.fileSize} MB!`)
          return false
        }
      }
      number.value++
      return true
    }

    // 文件个数超出
    const handleExceed = () => {
      ElMessage.error(`上传文件数量不能超过 ${props.limit} 个!`)
    }

    // 上传成功回调
    const handleUploadSuccess = (res, file) => {
      if (res.code === 200) {
        uploadList.value.push({ name: res.fileName, url: res.fileName })
        uploadedSuccessfully()
      } else {
        number.value--
        ElMessage.error(res.msg)
        handleDelete(file)
        uploadedSuccessfully()
      }
    }

    // 删除图片
    const handleDelete = (file) => {
      const findex = fileList.value.map(f => f.name).indexOf(file.name)
      if (findex > -1) {
        fileList.value.splice(findex, 1)
        emit("update:modelValue", listToString(fileList.value))
      }
    }

    // 上传失败
    const handleUploadError = () => {
      ElMessage.error("上传图片失败，请重试")
      number.value--
    }

    // 上传结束处理
    const uploadedSuccessfully = () => {
      if (number.value > 0 && uploadList.value.length === number.value) {
        fileList.value = [...fileList.value, ...uploadList.value]
        uploadList.value = []
        number.value = 0
        emit("update:modelValue", listToString(fileList.value))
      }
    }

    // 预览
    const handlePictureCardPreview = (file) => {
      dialogImageUrl.value = file.url
      dialogVisible.value = true
    }

    // 对象转成指定字符串分隔
    const listToString = (list, separator = ",") => {
      const urls = list.map(item => {
        if (item.url) {
          let url = item.url
          // 移除baseUrl部分
          if (baseUrl.value && url.startsWith(baseUrl.value)) {
            url = url.substring(baseUrl.value.length)
          }
          return url
        }
        return ""
      }).filter(url => url !== "")

      return urls.join(separator)
    }

    // 组件挂载后初始化拖拽排序
    onMounted(() => {
      // if (props.drag && !props.disabled) {
      //   nextTick(() => {
      //     const uploadElement = refs.imageUpload?.$el?.querySelector('.el-upload-list')
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

    return {
      fileList,
      headers,
      uploadImgUrl,
      showTip,
      dialogImageUrl,
      dialogVisible,
      handleBeforeUpload,
      handleExceed,
      handleUploadSuccess,
      handleDelete,
      handleUploadError,
      handlePictureCardPreview
    }
  }
})
</script>

<style scoped lang="scss">
.component-upload-image {
  // .el-upload--picture-card 控制加号部分
  :deep(.hide .el-upload--picture-card) {
    display: none;
  }

  :deep(.el-upload-list--picture-card.is-disabled + .el-upload--picture-card) {
    display: none !important;
  }

  // 去掉动画效果
  :deep(.el-list-enter-active),
  :deep(.el-list-leave-active) {
    transition: all 0s;
  }

  :deep(.el-list-enter),
  :deep(.el-list-leave-active) {
    opacity: 0;
    transform: translateY(0);
  }
}
</style>
