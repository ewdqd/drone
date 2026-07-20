<template>
  <div class="control-section component-gap">
    <h3>⚙️ 系统设置与控制</h3>
    
    <!-- PX4飞控设置 -->
    <div class="setup-module">
      <h4>🛸 PX4飞控设置</h4>
      <div class="setup-controls">
        <div class="control-group">
          <label>飞控模式:</label>
          <select v-model="selectedPX4Mode" class="control-select">
            <option value="">- 选择模式 -</option>
            <option value="MANUAL">手动模式</option>
            <option value="ALTCTL">高度控制</option>
            <option value="POSCTL">位置控制</option>
            <option value="OFFBOARD">Offboard模式</option>
            <option value="AUTO.MISSION">任务模式</option>
            <option value="AUTO.LOITER">悬停模式</option>
            <option value="AUTO.RTL">返航模式</option>
            <option value="AUTO.LAND">自动降落</option>
          </select>
          <button 
            @click="setPX4Mode" 
            class="btn btn-primary"
            :disabled="!isConnected || !selectedPX4Mode"
          >
            🎯 设置飞控模式
          </button>
        </div>
        
        <div class="control-group">
          <label>系统操作:</label>
          <div class="button-group">
            <button 
              @click="rebootPX4" 
              class="btn btn-warning"
              :disabled="!isConnected"
            >
              🔄 重启PX4
            </button>
            <button 
              @click="calibrateCompass" 
              class="btn btn-info"
              :disabled="!isConnected"
            >
              🧭 校准罗盘
            </button>
            <button 
              @click="calibrateAccelerometer" 
              class="btn btn-info"
              :disabled="!isConnected"
            >
              📊 校准加速度计
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 控制模式设置 -->
    <div class="setup-module">
      <h4>🎮 控制模式设置</h4>
      <div class="control-mode-grid">
        <div class="mode-card" :class="{ active: controlMode === 'RC_POS_CONTROL' }">
          <h5>遥控器位置控制</h5>
          <p>使用遥控器进行位置控制</p>
          <button 
            @click="setControlMode('RC_POS_CONTROL')"
            class="btn btn-sm"
            :disabled="!isConnected"
          >
            选择
          </button>
        </div>
        
        <div class="mode-card" :class="{ active: controlMode === 'COMMAND_CONTROL' }">
          <h5>命令控制模式</h5>
          <p>通过ROS命令控制无人机</p>
          <button 
            @click="setControlMode('COMMAND_CONTROL')"
            class="btn btn-sm"
            :disabled="!isConnected"
          >
            选择
          </button>
        </div>
        
        <div class="mode-card" :class="{ active: controlMode === 'LAND_CONTROL' }">
          <h5>降落控制模式</h5>
          <p>执行自动降落程序</p>
          <button 
            @click="setControlMode('LAND_CONTROL')"
            class="btn btn-sm"
            :disabled="!isConnected"
          >
            选择
          </button>
        </div>
      </div>
    </div>

    <!-- 安全设置 -->
    <div class="setup-module">
      <h4>🛡️ 安全设置</h4>
      <div class="safety-controls">
        <div class="safety-item">
          <label>紧急停止:</label>
          <button 
            @click="emergencyStop" 
            class="btn btn-danger"
            :disabled="!isConnected || !armed"
          >
            🚨 紧急停止
          </button>
        </div>
        
        <div class="safety-item">
          <label>停止控制状态:</label>
          <button 
            @click="stopControlState(true)" 
            class="btn btn-warning"
            :disabled="!isConnected"
          >
            ⏸️ 停止控制
          </button>
          <button 
            @click="stopControlState(false)" 
            class="btn btn-success"
            :disabled="!isConnected"
          >
            ▶️ 恢复控制
          </button>
        </div>
        
        <div class="safety-item">
          <label>故障保护:</label>
          <button 
            @click="enableFailsafe" 
            class="btn btn-info"
            :disabled="!isConnected"
          >
            🔒 启用故障保护
          </button>
        </div>
      </div>
    </div>

    <!-- 位置偏移设置 -->
    <div class="setup-module">
      <h4>📍 位置偏移设置</h4>
      <div class="offset-controls">
        <div class="offset-inputs">
          <div class="input-group">
            <label>X偏移 (m):</label>
            <input 
              type="number" 
              v-model.number="offset.x" 
              step="0.1"
              class="control-input"
              :disabled="!isConnected"
            >
          </div>
          <div class="input-group">
            <label>Y偏移 (m):</label>
            <input 
              type="number" 
              v-model.number="offset.y" 
              step="0.1"
              class="control-input"
              :disabled="!isConnected"
            >
          </div>
          <div class="input-group">
            <label>Z偏移 (m):</label>
            <input 
              type="number" 
              v-model.number="offset.z" 
              step="0.1"
              class="control-input"
              :disabled="!isConnected"
            >
          </div>
        </div>
        <button 
          @click="setOffsetPose" 
          class="btn btn-primary"
          :disabled="!isConnected"
        >
          📍 设置位置偏移
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SetupControl',
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
    controlState: {
      type: Number,
      required: true,
      default: 0
    }
  },
  data() {
    return {
      selectedPX4Mode: '',
      offset: {
        x: 0,
        y: 0,
        z: 0
      }
    };
  },
  computed: {
    controlMode() {
      const modes = {
        0: 'INIT',
        1: 'RC_POS_CONTROL',
        2: 'COMMAND_CONTROL',
        3: 'LAND_CONTROL'
      };
      return modes[this.controlState] || 'UNKNOWN';
    }
  },
  methods: {
    setPX4Mode() {
      this.$emit('send-setup', {
        cmd: 1, // SET_PX4_MODE
        px4_mode: this.selectedPX4Mode
      });
    },

    rebootPX4() {
      if (confirm('确定要重启PX4飞控吗？这将中断当前飞行任务。')) {
        this.$emit('send-setup', {
          cmd: 2 // REBOOT_PX4
        });
      }
    },

    calibrateCompass() {
      if (confirm('开始罗盘校准？请确保无人机在开阔场地，远离金属物体。')) {
        // 发送罗盘校准命令
        this.$emit('send-command', {
          Agent_CMD: 5, // User_Mode
          // 这里需要根据实际协议添加罗盘校准参数
        });
      }
    },

    calibrateAccelerometer() {
      if (confirm('开始加速度计校准？请将无人机水平放置。')) {
        // 发送加速度计校准命令
        this.$emit('send-command', {
          Agent_CMD: 5, // User_Mode
          // 这里需要根据实际协议添加加速度计校准参数
        });
      }
    },

    setControlMode(mode) {
      const modeMap = {
        'RC_POS_CONTROL': 1,
        'COMMAND_CONTROL': 2,
        'LAND_CONTROL': 3
      };
      
      this.$emit('send-setup', {
        cmd: 3, // SET_CONTROL_MODE
        control_state: modeMap[mode] || 2
      });
    },

    emergencyStop() {
      if (confirm('⚠️ 紧急停止！这将立即停止所有电机。确定继续？')) {
        this.$emit('send-setup', {
          cmd: 0, // ARMING
          arming: false
        });
        this.stopControlState(true);
      }
    },

    stopControlState(stop) {
      this.$emit('send-stop-control', stop);
    },

    enableFailsafe() {
      // 启用故障保护逻辑
      this.$emit('send-param-settings', {
        names: ['failsafe_enable'],
        values: ['true']
      });
    },

    setOffsetPose() {
      this.$emit('send-offset-pose', {
        x: this.offset.x,
        y: this.offset.y,
        z: this.offset.z,
        latitude: 0, // 可根据需要设置
        longitude: 0,
        altitude: 0
      });
    }
  },
  emits: [
    'send-setup',
    'send-command', 
    'send-param-settings',
    'send-offset-pose',
    'send-stop-control'
  ]
};
</script>

<style scoped>
.setup-module {
  margin-bottom: 25px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.setup-module h4 {
  margin: 0 0 15px 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.setup-controls {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.control-group {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.control-group label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 100px;
}

.control-select {
  padding: 8px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  background: white;
  min-width: 200px;
}

.button-group {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.control-mode-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.mode-card {
  background: white;
  padding: 15px;
  border-radius: 8px;
  border: 2px solid #e0e0e0;
  text-align: center;
  transition: all 0.3s;
}

.mode-card.active {
  border-color: #409EFF;
  background: #f0f7ff;
}

.mode-card h5 {
  margin: 0 0 8px 0;
  color: #2c3e50;
}

.mode-card p {
  margin: 0 0 12px 0;
  color: #7f8c8d;
  font-size: 12px;
}

.safety-controls {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.safety-item {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.safety-item label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 120px;
}

.offset-controls {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.offset-inputs {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 12px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.input-group label {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.control-input {
  padding: 8px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  background: white;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

.btn-sm {
  padding: 6px 12px;
  font-size: 12px;
}

.btn-primary { background: #409EFF; color: white; }
.btn-success { background: #52c41a; color: white; }
.btn-warning { background: #faad14; color: white; }
.btn-danger { background: #f5222d; color: white; }
.btn-info { background: #13c2c2; color: white; }

@media (max-width: 768px) {
  .control-group {
    flex-direction: column;
    align-items: stretch;
  }
  
  .control-group label {
    min-width: auto;
  }
  
  .control-mode-grid {
    grid-template-columns: 1fr;
  }
  
  .safety-item {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>