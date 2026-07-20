<template>
  <div class="control-section component-gap">
    <h3>🎮 综合控制中心</h3>
    
    <!-- 核心容器：包含视频流背景 + 所有控制功能 -->
    <div class="unified-control-container">
      <!-- 视频流背景（底层，从后端拉取） -->
      <video 
        ref="videoPlayer"
        class="video-background"
        :src="videoSource"
        autoplay
        muted
        loop
        playsinline
        :poster="videoPoster"
        controlsList="nodownload"
        preload="auto"
        @error="handleVideoError"
        @loadedmetadata="handleVideoLoaded"
        @loadeddata="handleVideoLoaded"
        @stalled="handleVideoError"
        @abort="handleVideoError"
      ></video>
      
      <!-- 视频加载状态提示 -->
      <div class="video-loading" v-if="isVideoLoading">
        <div class="loading-spinner"></div>
        <span>📹 正在拉取后端视频流...</span>
      </div>
      
      <!-- 视频加载失败提示 -->
      <div class="video-error" v-if="isVideoError">
        <span>❌ 视频流加载失败，请检查后端连接</span>
        <button @click="retryVideoLoad" class="retry-btn">重新加载</button>
      </div>
      
      <!-- 内容层（叠加在视频背景上） -->
      <div class="content-wrapper">
        <!-- 1. 视频流算法选择（顶部横向排列） -->
        <div class="algorithm-selection">
          <label class="algorithm-label">视频流算法:</label>
          <div class="algorithm-buttons">
            <button 
              @click="switchVideoAlgorithm('original')" 
              class="btn btn-sm btn-algorithm"
              :class="{ active: selectedAlgorithm === 'original' }"
              :disabled="!isConnected"
            >
              📹 原视频流
            </button>
            <button 
              @click="switchVideoAlgorithm('crowd_detection')" 
              class="btn btn-sm btn-algorithm"
              :class="{ active: selectedAlgorithm === 'crowd_detection' }"
              :disabled="!isConnected"
            >
              👥 人流检测
            </button>
            <button 
              @click="switchVideoAlgorithm('target_tracking')" 
              class="btn btn-sm btn-algorithm"
              :class="{ active: selectedAlgorithm === 'target_tracking' }"
              :disabled="!isConnected"
            >
              🎯 目标追踪
            </button>
          </div>
          <div class="algorithm-status" v-if="selectedAlgorithm">
            <span>当前：{{ getAlgorithmName(selectedAlgorithm) }}</span>
          </div>
        </div>

        <!-- 2. 控制参数配置（横向排列，紧凑布局） -->
        <div class="control-params">
          <div class="param-item">
            <label>坐标系:</label>
            <select 
              v-model="currentControlMode" 
              class="param-select"
              @change="handleControlModeChange"
              :disabled="!isConnected || armed"
            >
              <option value="ENU">ENU</option>
              <option value="BODY">机体</option>
            </select>
          </div>
          <div class="param-item">
            <label>步长(m):</label>
            <input 
              type="number" 
              v-model.number="currentStepSize" 
              class="param-input"
              min="0.1" 
              max="5" 
              step="0.1"
              @change="handleStepSizeChange"
              @blur="validateStepSize"
              :disabled="!isConnected"
            >
          </div>
          <div class="param-item">
            <label>偏航角(rad):</label>
            <input 
              type="number" 
              v-model.number="currentYawRef" 
              class="param-input"
              min="-3.14" 
              max="3.14" 
              step="0.1"
              @change="handleYawRefChange"
              @blur="validateYawRef"
              :disabled="!isConnected"
            >
          </div>
        </div>

        <!-- 3. 核心控制（无人机移动 + 云台调节，横向并列） -->
        <div class="core-controls">
          <!-- 无人机移动摇杆 -->
          <div class="joystick-control">
            <h5>无人机移动</h5>
            <div class="joystick-container">
              <div 
                class="joystick" 
                :class="{ disabled: !isConnected || !armed }"
                @mousedown="startJoystick"
                @touchstart="startJoystick"
                ref="joystick"
              >
                <div class="joystick-handle" :style="joystickPosition"></div>
              </div>
              <div class="joystick-center"></div>
            </div>
            <div class="direction-indicators">
              <div class="direction-indicator forward" :class="{ active: joystickDirection.forward }">↑</div>
              <div class="direction-indicator backward" :class="{ active: joystickDirection.backward }">↓</div>
              <div class="direction-indicator left" :class="{ active: joystickDirection.left }">←</div>
              <div class="direction-indicator right" :class="{ active: joystickDirection.right }">→</div>
            </div>
            <div class="joystick-status">
              <span>X: {{ joystickValues.x.toFixed(2) }}</span>
              <span>Y: {{ joystickValues.y.toFixed(2) }}</span>
            </div>
          </div>

          <!-- 云台调节控制 -->
          <div class="gimbal-control">
            <h5>云台调节</h5>
            <div class="gimbal-pad">
              <div class="pad-row">
                <button 
                  @click="gimbalMove('up')" 
                  class="btn btn-gimbal"
                  :disabled="!isConnected"
                >
                  ⬆️ 上
                </button>
              </div>
              <div class="pad-row">
                <button 
                  @click="gimbalMove('left')" 
                  class="btn btn-gimbal"
                  :disabled="!isConnected"
                >
                  ⬅️ 左
                </button>
                <button 
                  @click="gimbalMove('center')" 
                  class="btn btn-gimbal center"
                  :disabled="!isConnected"
                >
                  🎯 回中
                </button>
                <button 
                  @click="gimbalMove('right')" 
                  class="btn btn-gimbal"
                  :disabled="!isConnected"
                >
                  ➡️ 右
                </button>
              </div>
              <div class="pad-row">
                <button 
                  @click="gimbalMove('down')" 
                  class="btn btn-gimbal"
                  :disabled="!isConnected"
                >
                  ⬇️ 下
                </button>
              </div>
            </div>
            <div class="gimbal-settings">
              <div class="setting-group">
                <label>速度:</label>
                <input 
                  type="range" 
                  v-model.number="gimbalSpeed" 
                  min="1" 
                  max="10"
                  class="speed-slider"
                  :disabled="!isConnected"
                >
                <span>{{ gimbalSpeed }}</span>
              </div>
              <div class="setting-group">
                <button 
                  @click="toggleAttitudeCorrection" 
                  class="btn btn-sm"
                  :class="attitudeCorrection ? 'btn-success' : 'btn-secondary'"
                  :disabled="!isConnected"
                >
                  {{ attitudeCorrection ? '✅ 姿态修正' : '❌ 姿态修正' }}
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 4. 无人机升降控制（横向排列，与核心控制同容器） -->
        <div class="vertical-controls">
          <h5>无人机升降</h5>
          <div class="vertical-control-group">
            <button 
              @click="handleMoveUp" 
              class="btn btn-altitude up" 
              :disabled="!isConnected || !armed"
            >
              ⬆️ 上升
            </button>
            <div class="altitude-display">
              <div class="altitude-bar">
                <div class="altitude-fill" :style="{ height: altitudePercentage + '%' }"></div>
              </div>
              <span class="altitude-value">{{ currentAltitude.toFixed(1) }}m</span>
            </div>
            <button 
              @click="handleMoveDown" 
              class="btn btn-altitude down" 
              :disabled="!isConnected || !armed"
            >
              ⬇️ 下降
            </button>
            <button 
              @click="handleHover" 
              class="btn btn-hover" 
              :disabled="!isConnected || !armed"
            >
              ⏹️ 悬停
            </button>
          </div>
        </div>

        <!-- 5. 算法参数设置（底部横向排列） -->
        <div class="algorithm-params">
          <div class="param-group">
            <div v-if="selectedAlgorithm === 'crowd_detection'" class="param-item">
              <label>检测置信度:</label>
              <input 
                type="range" 
                v-model.number="algorithmParams.crowd.confidence" 
                min="0.1" 
                max="1.0" 
                step="0.1"
                class="param-slider"
                :disabled="!isConnected"
              >
              <span>{{ algorithmParams.crowd.confidence.toFixed(1) }}</span>
            </div>
            <div v-if="selectedAlgorithm === 'target_tracking'" class="param-item">
              <label>追踪阈值:</label>
              <input 
                type="range" 
                v-model.number="algorithmParams.tracking.threshold" 
                min="0.1" 
                max="1.0" 
                step="0.1"
                class="param-slider"
                :disabled="!isConnected"
              >
              <span>{{ algorithmParams.tracking.threshold.toFixed(1) }}</span>
            </div>
            <div class="param-item">
              <label>处理间隔(ms):</label>
              <input 
                type="number" 
                v-model.number="algorithmParams.common.interval" 
                min="10" 
                max="1000"
                class="param-input"
                :disabled="!isConnected"
              >
            </div>
          </div>
          <button 
            @click="applyAlgorithmParams" 
            class="btn btn-primary"
            :disabled="!isConnected || !selectedAlgorithm"
          >
            💾 应用参数
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MovementController',
  props: {
    isConnected: {
      type: Boolean,
      required: true,
      default: false
    },
    armed: {
      type: Boolean,
      required: true,
      default: false
    },
    controlMode: {
      type: String,
      required: true,
      default: 'ENU'
    },
    stepSize: {
      type: Number,
      required: true,
      default: 1.0
    },
    yawRef: {
      type: Number,
      required: true,
      default: 0
    },
    position: {
      type: Object,
      required: true,
      default: () => ({ x: 0, y: 0, z: 0 })
    },
    attitude: {
      type: Object,
      required: true,
      default: () => ({ roll: 0, pitch: 0, yaw: 0 })
    },
    // 后端视频流地址（父组件传递，含算法参数）
    videoSource: {
      type: String,
      required: true,
      default: 'http://192.168.144.60:8080/stream?algorithm=original'
    },
    // 视频封面（加载时显示）
    videoPoster: {
      type: String,
      default: 'https://via.placeholder.com/1200x800?text=视频流加载中...'
    }
  },
  data() {
    return {
      // 移动控制相关
      currentControlMode: this.controlMode,
      currentStepSize: this.stepSize,
      currentYawRef: this.yawRef,
      currentAltitude: 0,
      joystickActive: false,
      joystickValues: { x: 0, y: 0, intensity: 0 },
      joystickDirection: { forward: false, backward: false, left: false, right: false },
      
      // 视觉/云台相关
      selectedAlgorithm: 'original',
      gimbalSpeed: 5,
      attitudeCorrection: true,
      algorithmParams: {
        common: { interval: 100 },
        crowd: { confidence: 0.5 },
        tracking: { threshold: 0.7 }
      },
      
      // 视频流状态
      isVideoLoading: true,
      isVideoError: false,
      
      // 错误处理
      hasRuntimeError: false
    };
  },
  computed: {
    joystickPosition() {
      const maxOffset = 40;
      return {
        transform: `translate(${this.joystickValues.x * maxOffset}px, ${this.joystickValues.y * maxOffset}px)`
      };
    },
    altitudePercentage() {
      const maxAltitude = 10;
      return Math.min((this.currentAltitude / maxAltitude) * 100, 100);
    }
  },
  watch: {
    controlMode(newVal) {
      this.currentControlMode = newVal;
    },
    stepSize(newVal) {
      this.currentStepSize = newVal;
    },
    yawRef(newVal) {
      this.currentYawRef = newVal;
    },
    position: {
      handler(newVal) {
        this.currentAltitude = newVal.z;
      },
      deep: true,
      immediate: true
    },
    joystickValues: {
      handler(newVal) {
        if (this.joystickActive && newVal.intensity > 0.1) {
          this.sendJoystickCommand();
        }
      },
      deep: true
    },
    // 监听视频流地址变化，重新加载
    videoSource(newVal) {
      this.isVideoLoading = true;
      this.isVideoError = false;
      if (this.$refs.videoPlayer) {
        this.$refs.videoPlayer.load();
      }
    }
  },
  mounted() {
    try {
      // 绑定全局事件
      document.addEventListener('mousemove', this.handleJoystickMove);
      document.addEventListener('mouseup', this.stopJoystick);
      document.addEventListener('touchmove', this.handleJoystickMove, { passive: false });
      document.addEventListener('touchend', this.stopJoystick);
      
      // 初始化算法选择（从视频流地址解析）
      this.parseAlgorithmFromUrl();
      
      // 初始化视频播放器
      this.initVideoPlayer();
      
    } catch (error) {
      console.error('组件初始化错误:', error);
      this.hasRuntimeError = true;
    }
  },
  beforeDestroy() {
    try {
      // 移除全局事件
      document.removeEventListener('mousemove', this.handleJoystickMove);
      document.removeEventListener('mouseup', this.stopJoystick);
      document.removeEventListener('touchmove', this.handleJoystickMove);
      document.removeEventListener('touchend', this.stopJoystick);
    } catch (error) {
      console.warn('清理事件监听器时出错:', error);
    }
  },
  methods: {
    // 安全的组件初始化
    initVideoPlayer() {
      try {
        if (this.$refs.videoPlayer) {
          this.$refs.videoPlayer.load();
        }
      } catch (error) {
        console.error('初始化视频播放器失败:', error);
        this.isVideoError = true;
        this.isVideoLoading = false;
      }
    },

    // 从视频流URL解析当前算法
    parseAlgorithmFromUrl() {
      try {
        const url = new URL(this.videoSource);
        const algo = url.searchParams.get('algorithm');
        if (algo && ['original', 'crowd_detection', 'target_tracking'].includes(algo)) {
          this.selectedAlgorithm = algo;
        }
      } catch (e) {
        console.warn('解析视频流地址失败，使用默认算法', e);
        this.selectedAlgorithm = 'original';
      }
    },

    // 视频流事件处理
    handleVideoLoaded() {
      try {
        this.isVideoLoading = false;
        this.isVideoError = false;
        this.$emit('video-loaded'); // 通知父组件视频加载成功
      } catch (error) {
        console.error('处理视频加载事件时出错:', error);
      }
    },
    
    handleVideoError(event) {
      try {
        this.isVideoLoading = false;
        this.isVideoError = true;
        const errorMsg = event.target?.error?.message || '未知错误';
        this.$emit('video-error', errorMsg); // 通知父组件加载失败
        console.error('视频流加载失败:', errorMsg);
      } catch (error) {
        console.error('处理视频错误事件时出错:', error);
      }
    },
    
    retryVideoLoad() {
      try {
        this.isVideoLoading = true;
        this.isVideoError = false;
        if (this.$refs.videoPlayer) {
          this.$refs.videoPlayer.load();
        }
      } catch (error) {
        console.error('重试视频加载时出错:', error);
      }
    },

    // 移动控制参数处理
    handleControlModeChange() {
      this.$emit('change-control-mode', this.currentControlMode);
    },
    
    handleStepSizeChange() {
      this.currentStepSize = this.validateStepSize();
      this.$emit('change-step-size', this.currentStepSize);
    },
    
    validateStepSize() {
      try {
        const min = 0.1;
        const max = 5;
        let val = this.currentStepSize;
        if (isNaN(val) || val < min) val = min;
        if (val > max) val = max;
        this.currentStepSize = Number(val.toFixed(1));
        return this.currentStepSize;
      } catch (error) {
        console.error('验证步长时出错:', error);
        return 1.0;
      }
    },
    
    handleYawRefChange() {
      this.currentYawRef = this.validateYawRef();
      this.$emit('change-yaw-ref', this.currentYawRef);
    },
    
    validateYawRef() {
      try {
        const min = -Math.PI;
        const max = Math.PI;
        let val = this.currentYawRef;
        if (isNaN(val) || val < min) val = min;
        if (val > max) val = max;
        this.currentYawRef = Number(val.toFixed(2));
        return this.currentYawRef;
      } catch (error) {
        console.error('验证偏航角时出错:', error);
        return 0;
      }
    },

    // 摇杆控制
    startJoystick(event) {
      try {
        if (!this.isConnected || !this.armed) return;
        event.preventDefault();
        this.joystickActive = true;
        this.handleJoystickMove(event);
      } catch (error) {
        console.error('启动摇杆时出错:', error);
      }
    },
    
    handleJoystickMove(event) {
      try {
        if (!this.joystickActive) return;
        event.preventDefault();
        
        const joystick = this.$refs.joystick;
        if (!joystick) return;
        
        const rect = joystick.getBoundingClientRect();
        const centerX = rect.left + rect.width / 2;
        const centerY = rect.top + rect.height / 2;
        
        let clientX = event.type.includes('touch') ? event.touches[0].clientX : event.clientX;
        let clientY = event.type.includes('touch') ? event.touches[0].clientY : event.clientY;
        
        const deltaX = clientX - centerX;
        const deltaY = clientY - centerY;
        const distance = Math.min(Math.sqrt(deltaX * deltaX + deltaY * deltaY), rect.width / 2);
        
        const normalizedX = (deltaX / (rect.width / 2)) * (distance / (rect.width / 2));
        const normalizedY = (deltaY / (rect.height / 2)) * (distance / (rect.height / 2));
        
        this.joystickValues = {
          x: Math.max(-1, Math.min(1, normalizedX)),
          y: Math.max(-1, Math.min(1, normalizedY)),
          intensity: distance / (rect.width / 2)
        };
        
        this.updateDirectionIndicators();
      } catch (error) {
        console.error('处理摇杆移动时出错:', error);
      }
    },
    
    stopJoystick() {
      try {
        if (!this.joystickActive) return;
        this.joystickActive = false;
        this.joystickValues = { x: 0, y: 0, intensity: 0 };
        this.joystickDirection = { forward: false, backward: false, left: false, right: false };
        this.$emit('hover');
      } catch (error) {
        console.error('停止摇杆时出错:', error);
      }
    },
    
    updateDirectionIndicators() {
      try {
        const { x, y, intensity } = this.joystickValues;
        const threshold = 0.3;
        
        this.joystickDirection = {
          forward: y < -threshold && intensity > threshold,
          backward: y > threshold && intensity > threshold,
          left: x < -threshold && intensity > threshold,
          right: x > threshold && intensity > threshold
        };
      } catch (error) {
        console.error('更新方向指示器时出错:', error);
      }
    },
    
    sendJoystickCommand() {
      try {
        if (this.joystickDirection.forward && !this.joystickDirection.left && !this.joystickDirection.right) {
          this.$emit('move-forward');
        } else if (this.joystickDirection.backward && !this.joystickDirection.left && !this.joystickDirection.right) {
          this.$emit('move-backward');
        } else if (this.joystickDirection.left && !this.joystickDirection.forward && !this.joystickDirection.backward) {
          this.$emit('move-left');
        } else if (this.joystickDirection.right && !this.joystickDirection.forward && !this.joystickDirection.backward) {
          this.$emit('move-right');
        } else if (this.joystickDirection.forward && this.joystickDirection.left) {
          this.$emit('move-forward');
          this.$emit('move-left');
        } else if (this.joystickDirection.forward && this.joystickDirection.right) {
          this.$emit('move-forward');
          this.$emit('move-right');
        } else if (this.joystickDirection.backward && this.joystickDirection.left) {
          this.$emit('move-backward');
          this.$emit('move-left');
        } else if (this.joystickDirection.backward && this.joystickDirection.right) {
          this.$emit('move-backward');
          this.$emit('move-right');
        }
      } catch (error) {
        console.error('发送摇杆命令时出错:', error);
      }
    },

    // 移动控制方法
    handleMoveUp() {
      this.$emit('move-up');
    },
    
    handleMoveDown() {
      this.$emit('move-down');
    },
    
    handleHover() {
      this.$emit('hover');
    },

    // 视觉/云台控制
    getAlgorithmName(algorithm) {
      const nameMap = {
        original: '原视频流',
        crowd_detection: '人流检测',
        target_tracking: '目标追踪'
      };
      return nameMap[algorithm] || '未知';
    },
    
    switchVideoAlgorithm(algorithm) {
      try {
        this.selectedAlgorithm = algorithm;
        // 通知父组件切换算法（同步更新视频流地址）
        this.$emit('send-vision-control', {
          type: 'algorithm_switch',
          algorithm: algorithm
        });
        // 切换算法后重新加载视频流
        this.isVideoLoading = true;
        setTimeout(() => {
          if (this.$refs.videoPlayer) {
            this.$refs.videoPlayer.load();
          }
        }, 500);
      } catch (error) {
        console.error('切换视频算法时出错:', error);
      }
    },
    
    gimbalMove(direction) {
      try {
        const movements = {
          up: { pitch: -this.gimbalSpeed },
          down: { pitch: this.gimbalSpeed },
          left: { yaw: -this.gimbalSpeed },
          right: { yaw: this.gimbalSpeed },
          center: { pitch: 0, yaw: 0 }
        };
        this.$emit('send-gimbal-control', {
          action: direction,
          params: movements[direction] || {},
          speed: this.gimbalSpeed
        });
      } catch (error) {
        console.error('云台移动时出错:', error);
      }
    },
    
    toggleAttitudeCorrection() {
      try {
        this.attitudeCorrection = !this.attitudeCorrection;
        this.$emit('send-attitude-correction', this.attitudeCorrection);
      } catch (error) {
        console.error('切换姿态修正时出错:', error);
      }
    },
    
    applyAlgorithmParams() {
      try {
        let params = {
          algorithm: this.selectedAlgorithm,
          common: this.algorithmParams.common
        };
        
        if (this.selectedAlgorithm === 'crowd_detection') {
          params.crowd = this.algorithmParams.crowd;
        } else if (this.selectedAlgorithm === 'target_tracking') {
          params.tracking = this.algorithmParams.tracking;
        }
        
        this.$emit('apply-vision-params', params);
        // 应用参数后重新加载视频流
        this.isVideoLoading = true;
        setTimeout(() => {
          if (this.$refs.videoPlayer) {
            this.$refs.videoPlayer.load();
          }
        }, 500);
      } catch (error) {
        console.error('应用算法参数时出错:', error);
      }
    }
  },
  emits: [
    'change-control-mode', 'change-step-size', 'change-yaw-ref',
    'move-forward', 'move-backward', 'move-left', 'move-right',
    'move-up', 'move-down', 'hover',
    'send-vision-control', 'send-gimbal-control', 'send-attitude-correction', 'apply-vision-params',
    'video-loaded', 'video-error'
  ]
};
</script>

<style scoped>
/* 外层控制区样式 */
.control-section {
  margin-bottom: 20px;
}

.component-gap h3 {
  color: #2c3e50;
  font-size: 18px;
  margin: 0 0 15px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 统一控制容器（包含视频背景） */
.unified-control-container {
  position: relative;
  width: 100%;
  padding: 25px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  overflow: hidden;
  min-height: 600px;
}

/* 视频流背景（后端拉取） */
.video-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.25; /* 背景透明度，不影响前景操作 */
  z-index: 0;
  object-position: center;
}

/* 视频加载状态 */
.video-loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
  background: rgba(0,0,0,0.7);
  color: white;
  padding: 15px 30px;
  border-radius: 8px;
  font-size: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(255,255,255,0.3);
  border-radius: 50%;
  border-top-color: #409EFF;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 视频加载失败 */
.video-error {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
  background: rgba(231,76,60,0.8);
  color: white;
  padding: 15px 30px;
  border-radius: 8px;
  font-size: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.retry-btn {
  background: white;
  color: #e74c3c;
  border: none;
  padding: 6px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  margin-top: 8px;
}

.retry-btn:hover {
  background: #f8f9fa;
}

/* 内容层（叠加在视频上） */
.content-wrapper {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 1. 算法选择区 */
.algorithm-selection {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
  background: rgba(255,255,255,0.8);
  padding: 12px 20px;
  border-radius: 6px;
}

.algorithm-label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 80px;
}

.algorithm-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  flex: 1;
}

.btn-algorithm {
  background: #2c3e50;
  color: white;
  padding: 6px 14px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 13px;
}

.btn-algorithm.active {
  background: #409EFF;
  box-shadow: 0 2px 4px rgba(64,158,255,0.3);
}

.btn-algorithm:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

.algorithm-status {
  font-size: 13px;
  color: #666;
  background: rgba(255,255,255,0.9);
  padding: 4px 10px;
  border-radius: 12px;
}

/* 2. 控制参数配置区 */
.control-params {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  background: rgba(255,255,255,0.8);
  padding: 12px 20px;
  border-radius: 6px;
}

.param-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.param-item label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 70px;
  font-size: 13px;
}

.param-select, .param-input {
  padding: 6px 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 13px;
  background: white;
}

.param-select {
  width: 100px;
}

.param-input {
  width: 80px;
}

/* 3. 核心控制区（摇杆+云台） */
.core-controls {
  display: flex;
  gap: 40px;
  justify-content: center;
  flex-wrap: wrap;
  background: rgba(255,255,255,0.8);
  padding: 20px;
  border-radius: 6px;
}

.joystick-control, .gimbal-control {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  min-width: 220px;
}

.joystick-control h5, .gimbal-control h5, .vertical-controls h5 {
  color: #2c3e50;
  margin: 0;
  font-size: 14px;
  font-weight: 600;
}

/* 摇杆样式 */
.joystick-container {
  position: relative;
  width: 140px;
  height: 140px;
  background: #e9ecef;
  border-radius: 50%;
  box-shadow: inset 0 0 8px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  justify-content: center;
}

.joystick {
  position: relative;
  width: 110px;
  height: 110px;
  background: #495057;
  border-radius: 50%;
  cursor: pointer;
  touch-action: none;
  box-shadow: 0 3px 6px rgba(0,0,0,0.2);
}

.joystick.disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.joystick-handle {
  position: absolute;
  width: 35px;
  height: 35px;
  background: #3498db;
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 2px 4px rgba(0,0,0,0.3);
  transition: transform 0.1s ease;
}

.joystick-center {
  position: absolute;
  width: 8px;
  height: 8px;
  background: #e74c3c;
  border-radius: 50%;
  z-index: 2;
}

/* 方向指示器 */
.direction-indicators {
  display: grid;
  grid-template-areas: ". forward ." "left . right" ". backward .";
  gap: 8px;
  width: 100px;
}

.direction-indicator {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #dee2e6;
  border-radius: 4px;
  color: #6c757d;
  font-weight: bold;
  font-size: 14px;
  transition: all 0.2s ease;
}

.direction-indicator.active {
  background: #3498db;
  color: white;
  box-shadow: 0 0 6px rgba(52,152,219,0.5);
}

/* 摇杆状态 */
.joystick-status {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #2c3e50;
}

/* 云台控制样式 */
.gimbal-pad {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: center;
}

.pad-row {
  display: flex;
  gap: 8px;
}

.btn-gimbal {
  width: 60px;
  height: 45px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  background: #2c3e50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-gimbal.center {
  width: 70px;
}

.btn-gimbal:hover:not(:disabled) {
  background: #409EFF;
  transform: translateY(-2px);
}

.gimbal-settings {
  display: flex;
  gap: 15px;
  align-items: center;
  flex-wrap: wrap;
  width: 100%;
  justify-content: center;
}

.setting-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.speed-slider {
  width: 80px;
}

/* 4. 升降控制区 */
.vertical-controls {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  background: rgba(255,255,255,0.8);
  padding: 15px 20px;
  border-radius: 6px;
}

.vertical-control-group {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
  justify-content: center;
}

.btn-altitude {
  width: 80px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-altitude.up {
  background: #2ecc71;
}

.btn-altitude.down {
  background: #e74c3c;
}

.btn-altitude:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.altitude-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.altitude-bar {
  width: 18px;
  height: 100px;
  background: #e9ecef;
  border-radius: 10px;
  overflow: hidden;
  position: relative;
}

.altitude-fill {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background: linear-gradient(to top, #2ecc71, #f39c12, #e74c3c);
  transition: height 0.5s ease;
}

.altitude-value {
  font-size: 14px;
  font-weight: bold;
  color: #2c3e50;
}

.btn-hover {
  background: #7f8c8d;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 8px 20px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-hover:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 5. 算法参数区 */
.algorithm-params {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
  background: rgba(255,255,255,0.8);
  padding: 15px 20px;
  border-radius: 6px;
}

.param-group {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  flex: 1;
}

.param-slider {
  width: 120px;
}

.btn-primary {
  background: #409EFF;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 8px 16px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-sm {
  padding: 4px 10px;
  font-size: 12px;
}

.btn-success {
  background: #52c41a;
  color: white;
}

.btn-secondary {
  background: #f0f0f0;
  color: #666;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .unified-control-container {
    padding: 15px;
  }
  
  .core-controls {
    gap: 20px;
  }
  
  .vertical-control-group {
    gap: 15px;
  }
  
  .param-group {
    gap: 15px;
  }
  
  .video-loading, .video-error {
    font-size: 14px;
    padding: 12px 24px;
  }
}
</style>