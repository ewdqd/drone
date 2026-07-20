<template>
  <div id="app">
    <!-- 主布局：侧边栏 + 主内容区 -->
    <div class="main-layout">
      <!-- 侧边栏导航（固定） -->
      <div class="sidebar-nav">
        <div class="sidebar-header">
          <h1>🚁 P600 控制中心</h1>
        </div>
        <el-menu
          :default-active="$route.path"
          class="admin-menu"
          router
          background-color="#1a2b3c"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          active-background-color="#263445"
        >
          <el-menu-item index="/admin/home">
            <i class="el-icon-s-home"></i>
            <span>管理首页</span>
          </el-menu-item>
          <el-menu-item index="/admin/home/news-manage">
            <i class="el-icon-news"></i>
            <span>新闻管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/home/product-manage">
            <i class="el-icon-goods"></i>
            <span>产品管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/home/user-manage">
            <i class="el-icon-user"></i>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/home/setting">
            <i class="el-icon-setting"></i>
            <span>系统设置</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 主内容区 -->
      <div class="main-content">
        <!-- 管理页面区域 -->
        <div v-if="isManagementPage" class="management-area">
          <router-view></router-view>
        </div>

        <!-- 飞行控制区域（仅在管理首页显示） -->
        <div v-else class="control-area">
          <!-- 顶部小状态栏（恒显） -->
          <div class="status-bar">
            <div class="status-item">
              <label>ROS连接:</label>
              <span :class="{ 'connected': isConnected }">{{ isConnected ? '✅ 已连接' : '❌ 未连接' }}</span>
            </div>
            <div class="status-item">
              <label>无人机状态:</label>
              <span class="control-state">{{ uavStateText }}</span>
            </div>
            <div class="status-item">
              <label>飞行模式:</label>
              <span class="mode-value">{{ flightMode }}</span>
            </div>
            <div class="status-item">
              <label>电池电量:</label>
              <span class="battery" :class="getBatteryClass(batteryPercentage)">{{ Math.round(batteryPercentage) }}%</span>
            </div>
            <!-- 视频流状态显示 -->
            <div class="status-item">
              <label>视频流状态:</label>
              <span :class="videoStatusClass">{{ videoStatusText }}</span>
            </div>
          </div>

          <!-- 功能按钮组（恒显） -->
          <div class="function-buttons">
            <button 
              @click="switchComponent('flightControl')"
              :class="['btn', activeComponent === 'flightControl' ? 'btn-active' : '']"
            >
              🛫 飞行控制中心
            </button>
            <button 
              @click="switchComponent('movementController')"
              :class="['btn', activeComponent === 'movementController' ? 'btn-active' : '']"
            >
              🎮 综合控制中心
            </button>
             <button 
              @click="switchComponent('trajectoryPlanning')"
              :class="['btn', activeComponent === 'trajectoryPlanning' ? 'btn-active' : '']"
            >
              ✈️ 轨迹规划与栅格地图
            </button>
            <button 
              @click="switchComponent('parameterSettings')"
              :class="['btn', activeComponent === 'parameterSettings' ? 'btn-active' : '']"
            >
              ⚙️ 参数设置与偏移控制
            </button>
            <button 
              @click="switchComponent('customDataSegment')"
              :class="['btn', activeComponent === 'customDataSegment' ? 'btn-active' : '']"
            >
              📊 自定义数据段管理
            </button>
           
            <button 
              @click="switchComponent('egoPlanner')"
              :class="['btn', activeComponent === 'egoPlanner' ? 'btn-active' : '']"
            >
              🧭 自主规划器
            </button>
          </div>

          <!-- 动态内容区（按需显示） -->
          <div class="content-area">
            <!-- 飞行控制组件 -->
            <FlightControl
              v-if="activeComponent === 'flightControl'"
              :is-connected="isConnected"
              :armed="armed"
              :uav-connected="uavConnected"
              :control-state="controlState"
              :position="position"
              :attitude="attitude"
              @send-command="sendUAVCommand"
              @send-setup="sendSetup"
              @send-param-settings="sendParamSettings"
              @send-offset-pose="sendOffsetPose"
              @send-stop-control="sendStopControl"
            />

            <!-- 合并后的综合控制组件（传递后端视频流地址） -->
            <MovementController
              v-if="activeComponent === 'movementController'"
              :is-connected="isConnected"
              :armed="armed"
              :control-mode="controlMode"
              :step-size="stepSize"
              :yaw-ref="yawRef"
              :position="position"
              :attitude="attitude"
              :video-source="fullVideoSource"
              @change-control-mode="controlMode = $event"
              @change-step-size="stepSize = $event"
              @change-yaw-ref="yawRef = $event"
              @move-forward="moveForward"
              @move-backward="moveBackward"
              @move-left="moveLeft"
              @move-right="moveRight"
              @move-up="moveUp"
              @move-down="moveDown"
              @hover="sendHover"
              @send-vision-control="handleVisionControl"
              @send-gimbal-control="sendGimbalControl"
              @send-attitude-correction="sendAttitudeCorrection"
              @apply-vision-params="applyVisionParams"
              @video-loaded="handleVideoLoaded"
              @video-error="handleVideoError"
            />

            <!-- 其他原有组件 -->
            <ParameterSettings
              v-if="activeComponent === 'parameterSettings'"
              :is-connected="isConnected"
              @set-params="sendParamSettings"
            />

            <CustomDataSegment
              v-if="activeComponent === 'customDataSegment'"
              :is-connected="isConnected"
              :custom-data="customData"
              @add-custom-data="addCustomData"
              @remove-custom-data="removeCustomData"
              @send-custom-data="sendCustomDataSegment"
            />

            <!-- 轨迹规划组件 - 添加log-message事件监听 -->
            <TrajectoryPlanning
              v-if="activeComponent === 'trajectoryPlanning'"
              :is-connected="isConnected"
              :trajectory="trajectory"
              @send-trajectory="sendTrajectory"
              @update:trajectory="updateTrajectory"
              @log-message="handleLogMessage"
            />

            <EgoPlanner
              v-if="activeComponent === 'egoPlanner'"
              :is-connected="isConnected"
              @apply-gridmap-params="sendGridMapParams"
              @apply-planning-params="sendPlanningParams"
              @start-planning="startPlanning"
              @stop-planning="stopPlanning"
              @replan="replan"
              @clear-obstacles="clearObstacles"
              @set-planning-mode="setPlanningMode"
              @apply-visualization="applyVisualization"
            />
          </div>

          <!-- ROS连接控制 + 视频流配置 -->
          <div class="control-section">
            <h3>🔗 ROS连接控制</h3>
            <div class="connection-controls">
              <input v-model="rosBridgeUrl" placeholder="ws://localhost:9090" class="url-input">
              <button @click="connectROS" class="btn btn-connect">{{ isConnected ? '重新连接' : '连接' }}</button>
              <button @click="disconnectROS" class="btn btn-disconnect" :disabled="!isConnected">断开</button>
              <!-- 视频流地址配置 -->
              <div class="video-stream-config">
                <label>视频流地址:</label>
                <input 
                  v-model="backendVideoSource" 
                  placeholder="http://后端IP:端口/stream" 
                  class="stream-url-input"
                  @change="updateVideoSource"
                >
              </div>
              <!-- 算法选择（全局） -->
              <div class="global-algorithm-select">
                <label>全局算法:</label>
                <select v-model="globalAlgorithm" class="algorithm-select" @change="updateVideoSource">
                  <option value="original">原视频流</option>
                  <option value="crowd_detection">人流检测</option>
                  <option value="target_tracking">目标追踪</option>
                </select>
              </div>
            </div>
          </div>

          <!-- 操作日志 -->
          <div class="log-section">
            <div class="log-header">
              <h3>📝 操作日志</h3>
              <div class="log-controls">
                <button @click="clearLogs" class="btn btn-clear">清空日志</button>
                <span class="log-count">共 {{ logs.length }} 条</span>
              </div>
            </div>
            <div class="log-content">
              <div v-for="(log, index) in logs" :key="index" class="log-entry">{{ log }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ROSLIB from 'roslib';
// 导入所有组件
import FlightControl from './FlightControl.vue';
import MovementController from './MovementControl.vue';
import ParameterSettings from './ParameterSettings.vue';
import CustomDataSegment from './CustomDataSegment.vue';
import TrajectoryPlanning from './TrajectoryPlanning.vue';
import EgoPlanner from './EgoPlanner.vue';

export default {
  name: 'AdminHome',
  components: {
    FlightControl,
    MovementController,
    ParameterSettings,
    CustomDataSegment,
    TrajectoryPlanning,
    EgoPlanner
  },
  data() {
    return {
      activeComponent: 'flightControl', // 默认显示飞行控制
      ros: null,
      commandPublisher: null,
      stateSubscriber: null,
      controlStateSubscriber: null,
      feedbackSubscriber: null,
      trajectoryPublisher: null, // 新增：轨迹发布者
      trajectoryParamsPublisher: null, // 新增：轨迹参数发布者
      trajectoryStatusSubscriber: null, // 新增：轨迹状态订阅者
      isConnected: false,
      rosBridgeUrl: 'ws://192.168.144.60:9090',
      
      // 后端视频流核心配置
      backendVideoSource: 'http://192.168.144.60:8080/stream', // 基础地址
      globalAlgorithm: 'original', // 全局算法选择
      fullVideoSource: 'http://192.168.144.60:8080/stream?algorithm=original', // 完整地址（含算法参数）
      videoStatusText: '加载中',
      videoStatusClass: 'loading',
      
      // 控制参数
      controlMode: 'ENU',
      stepSize: 1.0,
      yawRef: 0,
      commandId: 1,
      
      // 无人机状态信息
      flightMode: '未知',
      position: { x: 0, y: 0, z: 0 },
      attitude: { roll: 0, pitch: 0, yaw: 0 },
      batteryPercentage: 0,
      batteryVoltage: 0,
      batteryText: '未知',
      uavConnected: false,
      armed: false,
      modeText: '未知',
      gpsStatus: 0,
      locationSource: 0,
      odomValid: false,
      gpsNum: 0,
      
      // 扩展状态字段
      uav_id: 1,
      range: 0,
      rel_alt: 0,
      velocity: [0, 0, 0],
      attitude_q: { x: 0, y: 0, z: 0, w: 1 },
      attitude_rate: [0, 0, 0],
      latitude: 0,
      longitude: 0,
      altitude: 0,
      
      // 其他业务数据
      customData: [],
      trajectory: { 
        header: { 
          seq: 0,
          stamp: { secs: 0, nsecs: 0 },
          frame_id: 'world' 
        },
        poses: [] 
      },
      trajectoryOptions: { // 新增：轨迹参数选项
        loop: false,
        velocity_control: false,
        velocity: 1.0,
        safe_altitude: 2.0
      },
      logs: [],
      
      // 控制状态标识
      controlState: 0
    };
  },
  computed: {
    // 无人机状态文本描述
    uavStateText() {
      if (!this.uavConnected) return '未连接';
      if (!this.armed) return '已锁定';
      return '已解锁';
    },
    // 判断是否为管理子页面（非首页）
    isManagementPage() {
      return this.$route.path !== '/admin/home';
    }
  },
  mounted() {
    // 页面加载时自动连接ROS
    this.connectROS();
    // 初始化视频流地址
    this.updateVideoSource();
    // 初始化视频流日志
    this.addLog(`📹 初始视频流地址：${this.fullVideoSource}`);
  },
  methods: {
    // 切换功能组件
    switchComponent(componentName) {
      this.activeComponent = componentName;
      this.addLog(`🔄 切换至【${this.getComponentName(componentName)}】`);
    },

    // 获取组件中文名称（日志显示用）
    getComponentName(componentName) {
      const nameMap = {
        flightControl: '飞行控制中心',
        movementController: '综合控制中心',
        parameterSettings: '参数设置与偏移控制',
        customDataSegment: '自定义数据段管理',
        trajectoryPlanning: '轨迹规划与栅格地图',
        egoPlanner: '自主规划器'
      };
      return nameMap[componentName] || componentName;
    },

    // ROS连接管理
    connectROS() {
      try {
        if (this.ros) {
          this.ros.close();
        }

        this.addLog(`正在连接ROS Bridge：${this.rosBridgeUrl}`);
        
        this.ros = new ROSLIB.Ros({
          url: this.rosBridgeUrl
        });

        // 连接成功回调
        this.ros.on('connection', () => {
          this.isConnected = true;
          this.addLog('✅ ROS Bridge连接成功');
          this.setupTopics(); // 订阅/发布话题初始化
          // 若当前在综合控制页，触发视频流拉取提示
          if (this.activeComponent === 'movementController') {
            this.addLog('📹 开始拉取后端视频流...');
          }
        });

        // 连接错误回调
        this.ros.on('error', (error) => {
          this.isConnected = false;
          this.addLog(`❌ ROS连接错误：${error.message}`);
        });

        // 连接关闭回调
        this.ros.on('close', () => {
          this.isConnected = false;
          this.addLog('🔌 ROS连接已关闭');
        });

      } catch (error) {
        this.addLog(`❌ ROS连接初始化失败：${error.message}`);
      }
    },
    
    // 断开ROS连接
    disconnectROS() {
      if (this.ros) {
        this.ros.close();
      }
      this.isConnected = false;
      this.addLog('🛑 手动断开ROS连接');
    },

    // 初始化ROS话题（订阅/发布）
    setupTopics() {
      // 1. 创建命令发布者（控制无人机用）
      this.commandPublisher = new ROSLIB.Topic({
        ros: this.ros,
        name: '/web_control/command',
        messageType: 'prometheus_msgs/UAVCommand'
      });
      
      // 2. 订阅无人机状态话题
      this.stateSubscriber = new ROSLIB.Topic({
        ros: this.ros,
        name: '/uav1/prometheus/state',
        messageType: 'prometheus_msgs/UAVState'
      });

      // 3. 订阅控制状态话题
      this.controlStateSubscriber = new ROSLIB.Topic({
        ros: this.ros,
        name: '/uav1/prometheus/control_state',
        messageType: 'prometheus_msgs/UAVControlState'
      });

      // 4. 订阅反馈消息话题
      this.feedbackSubscriber = new ROSLIB.Topic({
        ros: this.ros,
        name: '/web_control/feedback',
        messageType: 'std_msgs/String'
      });

      // 5. 创建轨迹发布者
      this.trajectoryPublisher = new ROSLIB.Topic({
        ros: this.ros,
        name: '/web_control/trajectory',
        messageType: 'nav_msgs/Path'
      });

      // 6. 创建轨迹参数发布者
      this.trajectoryParamsPublisher = new ROSLIB.Topic({
        ros: this.ros,
        name: '/web_control/trajectory_params',
        messageType: 'std_msgs/String'
      });

      // 7. 订阅轨迹执行状态
      this.trajectoryStatusSubscriber = new ROSLIB.Topic({
        ros: this.ros,
        name: '/uav1/prometheus/trajectory_status',
        messageType: 'std_msgs/String'
      });

      // 反馈消息回调（日志显示）
      this.feedbackSubscriber.subscribe((message) => {
        this.addLog(`🔔 系统反馈：${message.data}`);
      });

      // 无人机状态更新回调
      this.stateSubscriber.subscribe((message) => {
        this.updateUAVState(message);
      });

      // 控制状态更新回调
      this.controlStateSubscriber.subscribe((message) => {
        this.controlState = message.control_state || 0;
      });

      // 轨迹状态更新回调
      this.trajectoryStatusSubscriber.subscribe((message) => {
        try {
          const status = JSON.parse(message.data);
          this.addLog(`📊 轨迹状态更新：${status.state} - 进度 ${status.progress}%`);
        } catch (error) {
          this.addLog(`📊 轨迹状态：${message.data}`);
        }
      });

      this.addLog('✅ ROS话题订阅/发布初始化完成');
    },
    
    // 更新无人机状态数据
    updateUAVState(message) {
      // 基础连接/锁定状态
      this.uav_id = message.uav_id || 1;
      this.uavConnected = message.connected || false;
      this.armed = message.armed || false;
      
      // 飞行模式
      this.modeText = message.mode || '未知';
      this.flightMode = this.modeText;
      
      // 定位相关
      this.locationSource = message.location_source || 0;
      this.odomValid = message.odom_valid || false;
      this.gpsStatus = message.gps_status || 0;
      this.gpsNum = message.gps_num || 0;
      
      // 位置信息（XYZ）
      if (message.position && Array.isArray(message.position) && message.position.length >= 3) {
        this.position = {
          x: message.position[0] || 0,
          y: message.position[1] || 0,
          z: message.position[2] || 0
        };
      }
      
      // 高度/距离信息
      this.range = message.range || 0;
      this.rel_alt = message.rel_alt || 0;
      
      // 经纬度
      this.latitude = message.latitude || 0;
      this.longitude = message.longitude || 0;
      this.altitude = message.altitude || 0;
      
      // 姿态信息
      if (message.attitude && Array.isArray(message.attitude) && message.attitude.length >= 3) {
        this.attitude = {
          roll: message.attitude[0] || 0,
          pitch: message.attitude[1] || 0,
          yaw: message.attitude[2] || 0
        };
      }
      
      // 速度/姿态四元数/姿态角速度
      this.velocity = message.velocity || [0, 0, 0];
      this.attitude_q = message.attitude_q || { x: 0, y: 0, z: 0, w: 1 };
      this.attitude_rate = message.attitude_rate || [0, 0, 0];
      
      // 电池信息
      this.processBatteryInfo(message);
    },
    
    // 处理电池状态信息
    processBatteryInfo(message) {
      if (message.battery_percetage !== undefined) {
        // 兼容0-1和0-100两种格式
        this.batteryPercentage = message.battery_percetage <= 1.0 
          ? Math.round(message.battery_percetage * 100) 
          : Math.round(message.battery_percetage);
      } else {
        this.batteryPercentage = 0;
      }
      
      this.batteryVoltage = message.battery_state || 0;
      this.batteryText = this.getBatteryText(this.batteryPercentage);
    },
    
    // 电池状态文本描述
    getBatteryText(percentage) {
      if (percentage <= 0) return '未知';
      if (percentage > 70) return '良好';
      if (percentage > 30) return '一般';
      if (percentage > 10) return '低电量';
      return '严重低电量';
    },

    // 发送无人机控制命令（核心方法）
    sendUAVCommand(commandData) {
      if (!this.isConnected || !this.commandPublisher) {
        this.addLog('❌ 发送命令失败：ROS未连接或发布者未就绪');
        return;
      }

      // 构造ROS消息时间戳
      const now = new Date();
      const secs = Math.floor(now.getTime() / 1000);
      const nsecs = (now.getTime() % 1000) * 1000000;

      // 构造命令消息
      const message = new ROSLIB.Message({
        header: {
          stamp: { secs: secs, nsecs: nsecs },
          frame_id: commandData.frame_id || 'world'
        },
        Agent_CMD: commandData.Agent_CMD,
        Control_Level: commandData.Control_Level || 0,
        Move_mode: commandData.Move_mode || 0,
        position_ref: [
          commandData.position_ref?.x || 0,
          commandData.position_ref?.y || 0,
          commandData.position_ref?.z || 0
        ],
        velocity_ref: [0, 0, 0],
        acceleration_ref: [0, 0, 0],
        yaw_ref: commandData.yaw_ref || 0,
        Yaw_Rate_Mode: false,
        yaw_rate_ref: 0,
        att_ref: [0, 0, 0, 1],
        latitude: 0,
        longitude: 0,
        altitude: 0,
        Command_ID: this.commandId
      });

      // 发布命令
      this.commandPublisher.publish(message);
      this.addLog(`📤 发送命令 [ID: ${this.commandId}] - ${this.getCommandText(commandData.Agent_CMD)}`);
      this.commandId++;
    },

    // 基础飞行命令（封装）
    sendArm() {
      this.sendUAVCommand({
        Agent_CMD: 1,
        Control_Level: 0,
        Move_mode: 0,
        position_ref: { x: 0, y: 0, z: 0 },
        frame_id: 'ENU',
        yaw_ref: this.yawRef
      });
    },

    sendTakeoff() {
      this.sendUAVCommand({
        Agent_CMD: 1,
        Control_Level: 0,
        Move_mode: 0,
        position_ref: { x: this.position.x, y: this.position.y, z: 1.5 },
        frame_id: 'ENU',
        yaw_ref: this.yawRef
      });
    },

    sendLand() {
      this.sendUAVCommand({
        Agent_CMD: 3,
        Control_Level: 0,
        frame_id: 'ENU'
      });
    },

    sendDisarm() {
      this.sendUAVCommand({
        Agent_CMD: 4,
        Control_Level: 0,
        frame_id: 'ENU'
      });
    },

    sendHover() {
      this.sendUAVCommand({
        Agent_CMD: 2,
        Control_Level: 0,
        Move_mode: 0,
        position_ref: this.position,
        frame_id: 'ENU',
        yaw_ref: this.attitude.yaw
      });
    },

    // 方向移动控制（前后左右上下）
    moveForward() {
      this.sendDirectionMove('forward', this.stepSize);
    },

    moveBackward() {
      this.sendDirectionMove('backward', this.stepSize);
    },

    moveLeft() {
      this.sendDirectionMove('left', this.stepSize);
    },

    moveRight() {
      this.sendDirectionMove('right', this.stepSize);
    },

    moveUp() {
      this.sendDirectionMove('up', this.stepSize);
    },

    moveDown() {
      this.sendDirectionMove('down', this.stepSize);
    },

    // 方向移动命令封装（适配不同坐标系）
    sendDirectionMove(direction, step) {
      let positionRef = { ...this.position };
      let moveMode = 0;
      
      if (this.controlMode === 'ENU') {
        // ENU坐标系（东-北-天）
        switch (direction) {
          case 'forward': positionRef.y += step; break;
          case 'backward': positionRef.y -= step; break;
          case 'left': positionRef.x -= step; break;
          case 'right': positionRef.x += step; break;
          case 'up': positionRef.z += step; break;
          case 'down': positionRef.z -= step; break;
        }
        moveMode = 0;
      } else {
        // 机体坐标系（前-右-上）
        positionRef = { x: 0, y: 0, z: 0 };
        switch (direction) {
          case 'forward': positionRef.x = step; break;
          case 'backward': positionRef.x = -step; break;
          case 'left': positionRef.y = step; break;
          case 'right': positionRef.y = -step; break;
          case 'up': positionRef.z = step; break;
          case 'down': positionRef.z = -step; break;
        }
        moveMode = 3;
      }

      this.sendUAVCommand({
        Agent_CMD: 4,
        Control_Level: 0,
        Move_mode: moveMode,
        position_ref: positionRef,
        frame_id: this.controlMode,
        yaw_ref: this.yawRef
      });
    },

    // 工具方法：获取命令中文描述
    getCommandText(cmd) {
      const commands = {
        1: '初始位置悬停',
        2: '当前位置悬停',
        3: '降落',
        4: '移动',
        5: '用户模式'
      };
      return commands[cmd] || `未知命令(${cmd})`;
    },

    // 工具方法：获取电池状态样式类
    getBatteryClass(level) {
      if (level <= 0) return 'unknown';
      if (level > 70) return 'high';
      if (level > 30) return 'medium';
      return 'low';
    },

    // 视频流核心方法
    updateVideoSource() {
      // 拼接完整视频流地址（基础地址 + 算法参数）
      this.fullVideoSource = `${this.backendVideoSource}?algorithm=${this.globalAlgorithm}`;
      this.addLog(`📹 视频流地址更新为：${this.fullVideoSource}`);
      // 切换地址后重置状态
      if (this.activeComponent === 'movementController') {
        this.videoStatusText = '加载中';
        this.videoStatusClass = 'loading';
      }
    },
    handleVideoLoaded() {
      this.videoStatusText = '正常';
      this.videoStatusClass = 'success';
      this.addLog('✅ 视频流加载成功');
    },
    handleVideoError(msg) {
      this.videoStatusText = '异常';
      this.videoStatusClass = 'error';
      this.addLog(`❌ 视频流加载失败：${msg}`);
    },
    handleVisionControl(params) {
      // 同步全局算法选择
      this.globalAlgorithm = params.algorithm;
      this.updateVideoSource();
      this.addLog(`🎥 切换视频流算法：${this.getAlgorithmName(params.algorithm)}`);
    },

    // 轨迹规划相关方法
    sendTrajectory(trajectoryData) {
      if (!this.isConnected) {
        this.addLog('❌ 发送轨迹失败：ROS未连接');
        return;
      }

      try {
        // 创建轨迹发布者（如果不存在）
        if (!this.trajectoryPublisher) {
          this.trajectoryPublisher = new ROSLIB.Topic({
            ros: this.ros,
            name: '/web_control/trajectory',
            messageType: 'nav_msgs/Path'
          });
        }

        // 转换轨迹数据格式
        const trajectoryMsg = new ROSLIB.Message({
          header: trajectoryData.header,
          poses: trajectoryData.poses.map(pose => ({
            header: { frame_id: 'world' },
            pose: {
              position: {
                x: pose.pose.position.lng,
                y: pose.pose.position.lat, 
                z: pose.pose.position.z
              },
              orientation: pose.pose.orientation
            }
          }))
        });

        // 发布轨迹
        this.trajectoryPublisher.publish(trajectoryMsg);
        this.addLog(`✈️ 发送轨迹规划：共${trajectoryData.poses.length}个路径点`);

        // 同时发送轨迹参数
        this.sendTrajectoryParams();

      } catch (error) {
        this.addLog(`❌ 发送轨迹失败：${error.message}`);
      }
    },

    // 发送轨迹参数
    sendTrajectoryParams() {
      if (!this.isConnected) return;

      try {
        if (!this.trajectoryParamsPublisher) {
          this.trajectoryParamsPublisher = new ROSLIB.Topic({
            ros: this.ros,
            name: '/web_control/trajectory_params',
            messageType: 'std_msgs/String'
          });
        }

        const params = {
          loop: this.trajectoryOptions?.loop || false,
          velocity_control: this.trajectoryOptions?.velocity_control || false,
          velocity: this.trajectoryOptions?.velocity || 1.0,
          safe_altitude: this.trajectoryOptions?.safe_altitude || 2.0
        };

        const paramsMsg = new ROSLIB.Message({
          data: JSON.stringify(params)
        });

        this.trajectoryParamsPublisher.publish(paramsMsg);
        this.addLog(`⚙️ 发送轨迹参数：${JSON.stringify(params)}`);

      } catch (error) {
        this.addLog(`❌ 发送轨迹参数失败：${error.message}`);
      }
    },

    // 处理轨迹规划组件的日志消息
    handleLogMessage(message) {
      this.addLog(message);
    },

    // 其他业务方法
    sendParamSettings(params) {
      this.addLog(`⚙️ 发送参数设置：${JSON.stringify(params)}`);
    },

    sendCustomDataSegment() {
      if (this.customData.length === 0) return this.addLog('❌ 无自定义数据可发送');
      this.addLog(`📤 发送自定义数据段：${JSON.stringify(this.customData)}`);
    },

    addCustomData(data) {
      this.customData.push(data);
      this.addLog(`📥 添加自定义数据：${JSON.stringify(data)}`);
    },

    removeCustomData(index) {
      const removed = this.customData.splice(index, 1);
      this.addLog(`🗑️ 删除自定义数据：${JSON.stringify(removed[0])}`);
    },

    updateTrajectory(newTrajectory) {
      this.trajectory = newTrajectory;
      this.addLog(`🔄 更新轨迹规划：共${newTrajectory.poses?.length || 0}个路径点`);
    },

    // 系统设置相关
    sendSetup(params) {
      this.addLog(`⚙️ 发送系统设置：${JSON.stringify(params)}`);
    },

    sendOffsetPose(params) {
      this.addLog(`📍 发送位置偏移：${JSON.stringify(params)}`);
    },

    sendStopControl(stop) {
      this.addLog(`⏸️ ${stop ? '停止' : '恢复'}无人机控制`);
    },

    // 自主规划器相关
    sendGridMapParams(params) {
      this.addLog(`🗺️ 发送栅格地图参数：${JSON.stringify(params)}`);
    },

    sendPlanningParams(params) {
      this.addLog(`⚙️ 发送规划参数：${JSON.stringify(params)}`);
    },

    startPlanning() {
      this.addLog('🚀 启动自主规划');
    },

    stopPlanning() {
      this.addLog('⏹️ 停止自主规划');
    },

    replan() {
      this.addLog('🔄 重新规划路径');
    },

    clearObstacles() {
      this.addLog('🗑️ 清空障碍物数据');
    },

    setPlanningMode(mode) {
      this.addLog(`🎯 设置规划模式：${mode}`);
    },

    applyVisualization(params) {
      this.addLog(`👁️ 应用可视化设置：${JSON.stringify(params)}`);
    },

    // 综合控制组件回调（视觉/云台）
    sendGimbalControl(params) {
      this.addLog(`📷 云台${params.action}：参数${JSON.stringify(params.params)}，速度${params.speed}`);
    },

    sendAttitudeCorrection(enabled) {
      this.addLog(`🧭 ${enabled ? '启用' : '禁用'}姿态修正功能`);
    },

    applyVisionParams(params) {
      const algoName = this.getAlgorithmName(params.algorithm);
      this.addLog(`⚙️ 应用${algoName}参数：${JSON.stringify(params)}`);
    },

    // 工具方法：获取算法中文名称
    getAlgorithmName(algorithm) {
      const nameMap = {
        original: '原视频流',
        crowd_detection: '人流检测',
        target_tracking: '目标追踪'
      };
      return nameMap[algorithm] || '未知算法';
    },

    // 日志管理
    addLog(message) {
      const timestamp = new Date().toLocaleTimeString();
      this.logs.unshift(`[${timestamp}] ${message}`);
      // 日志上限100条，超出自动删除最旧的
      if (this.logs.length > 100) {
        this.logs.pop();
      }
    },

    clearLogs() {
      this.logs = [];
      this.addLog('📝 操作日志已清空');
    }
  },
  beforeDestroy() {
    // 页面销毁时清理资源
    if (this.stateSubscriber) this.stateSubscriber.unsubscribe();
    if (this.controlStateSubscriber) this.controlStateSubscriber.unsubscribe();
    if (this.feedbackSubscriber) this.feedbackSubscriber.unsubscribe();
    if (this.trajectoryStatusSubscriber) this.trajectoryStatusSubscriber.unsubscribe();
    if (this.ros) this.ros.close();
    this.addLog('🔌 页面销毁，资源已释放');
  }
};
</script>

<style scoped>
/* 全局样式重置与基础设置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Microsoft YaHei', 'Arial', sans-serif;
}

/* 主布局样式 */
.main-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

/* 侧边栏样式 */
.sidebar-nav {
  width: 240px;
  background-color: #1a2b3c;
  color: white;
  height: 100%;
  position: fixed;
  z-index: 10;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #263445;
}

.sidebar-header h1 {
  font-size: 18px;
  margin: 0;
  text-align: center;
  color: white;
  font-weight: 600;
}

.admin-menu {
  border-right: none;
  margin-top: 20px;
}

.el-menu-item {
  height: 50px !important;
  line-height: 50px !important;
}

.el-menu-item i {
  font-size: 16px !important;
}

.el-menu-item span {
  font-size: 14px !important;
}

/* 主内容区样式 */
.main-content {
  flex: 1;
  margin-left: 240px;
  padding: 20px;
  background-color: #f8f9fa;
  overflow-y: auto;
  height: 100vh;
}

/* 管理页面区域样式 */
.management-area {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  min-height: 600px;
  padding: 20px;
}

/* 控制区域样式 */
.status-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding: 15px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  margin-bottom: 20px;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  padding: 4px 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.status-item label {
  font-weight: 600;
  color: #7f8c8d;
  min-width: 80px;
}

.status-item span {
  color: #2c3e50;
  font-weight: 500;
}

.status-item .connected {
  color: #2ecc71;
  font-weight: 600;
}

.battery.high {
  color: #2ecc71;
}

.battery.medium {
  color: #f39c12;
}

.battery.low {
  color: #e74c3c;
  font-weight: 600;
}

.battery.unknown {
  color: #95a5a6;
}

/* 视频流状态样式 */
.videoStatusClass {
  font-weight: 600;
}
.videoStatusClass.success {
  color: #2ecc71;
}
.videoStatusClass.error {
  color: #e74c3c;
}
.videoStatusClass.loading {
  color: #3498db;
}

/* 功能按钮组样式 */
.function-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.btn {
  padding: 10px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.btn-active {
  background: #409EFF;
  color: white;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.btn:not(.btn-active) {
  background: white;
  color: #2c3e50;
  border: 1px solid #e0e0e0;
}

.btn:not(.btn-active):hover {
  background: #f8f9fa;
  border-color: #d0d0d0;
  transform: translateY(-2px);
}

/* 内容区域样式 */
.content-area {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  padding: 20px;
  margin-bottom: 20px;
  min-height: 400px;
}

/* ROS连接控制样式 */
.control-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  margin-bottom: 20px;
}

.control-section h3 {
  margin: 0 0 15px 0;
  color: #2c3e50;
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.connection-controls {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: center;
}

.url-input {
  padding: 10px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  flex: 1;
  min-width: 200px;
  font-size: 14px;
}

.btn-connect {
  background: #2c3e50;
  color: white;
  padding: 10px 20px;
}

.btn-disconnect {
  background: #95a5a6;
  color: white;
  padding: 10px 20px;
}

/* 视频流配置样式 */
.video-stream-config {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-width: 250px;
  margin-top: 10px;
}

.video-stream-config label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 80px;
}

.stream-url-input {
  padding: 10px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  flex: 1;
  font-size: 14px;
}

/* 全局算法选择样式 */
.global-algorithm-select {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-width: 200px;
  margin-top: 10px;
}

.global-algorithm-select label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 80px;
}

.algorithm-select {
  padding: 10px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  flex: 1;
  font-size: 14px;
}

/* 日志区域样式 */
.log-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.log-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.log-header h3 {
  margin: 0;
  color: #2c3e50;
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.log-controls {
  display: flex;
  gap: 10px;
  align-items: center;
}

.btn-clear {
  background: #bdc3c7;
  color: #2c3e50;
  padding: 6px 12px;
  font-size: 13px;
}

.log-count {
  font-size: 13px;
  color: #7f8c8d;
}

.log-content {
  height: 200px;
  overflow-y: auto;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 15px;
  background: #f8f9fa;
  font-family: monospace;
  font-size: 12px;
  line-height: 1.6;
}

.log-entry {
  padding: 5px 0;
  border-bottom: 1px solid #e9ecef;
  color: #2c3e50;
}

.log-entry:nth-child(odd) {
  background: rgba(255, 255, 255, 0.5);
}

/* 响应式适配 */
@media (max-width: 1200px) {
  .status-bar {
    gap: 10px;
  }
  
  .function-buttons {
    gap: 8px;
  }
  
  .function-buttons .btn {
    padding: 8px 12px;
    font-size: 13px;
  }
}

@media (max-width: 768px) {
  .sidebar-nav {
    width: 60px;
  }

  .sidebar-header h1, .el-menu-item span {
    display: none;
  }

  .main-content {
    margin-left: 60px;
    padding: 15px;
  }

  .status-item {
    width: 100%;
  }

  .function-buttons {
    flex-direction: column;
  }

  .function-buttons .btn {
    width: 100%;
    justify-content: center;
  }

  .connection-controls {
    flex-direction: column;
    align-items: stretch;
  }
  
  .content-area {
    padding: 15px;
  }
  
  .management-area {
    padding: 15px;
  }
  
  .video-stream-config, .global-algorithm-select {
    width: 100%;
  }
}
</style>