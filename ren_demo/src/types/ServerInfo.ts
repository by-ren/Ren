export interface ComputerSystemInfoBO {
	manufacturer: string;
	model: string;
	serialNumber: string;
	family: string;
	osManufacturer: string;
	version: string;
	bitness: Number;
	systemUptime: Number;
	processCount: Number;
	computerName: string;
	computerIp: string;
	osArch: string;
}

export interface CpuInfoBO {
	cpuNum: Number;
	cpuModel: string;
	used: string;
	waitUsed: string;
	userUsed: string;
	sysUsed: string;
	free: string;
}

export interface DiskInfoBO {
	dirName: string;
	sysTypeName: string;
	typeName: string;
	total: string;
	free: string;
	used: string;
	usage: string;
}

export interface JavaInfoBO {
	javaVersion: string;
	javaVersionInt: Number;
	javaVendor: string;
	runtimeName: string;
	runtimeVersion: string;
	javaHome: string;
	javaSpecName: string;
	javaSpecVersion: string;
	javaSpecVendor: string;
}

export interface JvmInfoBO {
	jvmName: string;
	jvmVersion: string;
	jvmVendor: string;
	jvmInfoStr: string;
	jvmSpecName: string;
	jvmSpecVersion: string;
	jvmSpecVendor: string;
	inputArgs: string;
}

export interface MemoryInfoBO {
	total: String;
	available: String;
	used: String;
	occupancyRate: string;
	jvmTotal: String;
	jvmAvailable: String;
	jvmUsed: String;
	jvmOccupancyRate: string;
	jvmMax: String;
}

export interface NetWorkInfoBO {
	name: string;
	displayName: string;
	macaddr: string;
	iPv4addr: string;
	iPv6addr: string;
	bytesSent: Number;
	bytesRecv: Number;
	packetsSent: Number;
	packetsRecv: Number;
}

export interface ProjectInfoBO {
	projectDir: string;
	startTimeStr: string;
	runningTimeStr: string;
}
