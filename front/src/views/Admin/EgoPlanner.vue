<template>
  <div class="control-section component-gap">
    <h3>🧭 自主规划器 (Ego Planner)</h3>
    
    <!-- 规划状态显示 -->
    <div class="planner-status-module">
      <h4>📊 规划器状态</h4>
      <div class="status-grid">
        <div class="status-item">
          <label>规划状态:</label>
          <span :class="['status-badge', planningStatus.class]">
            {{ planningStatus.text }}
          </span>
        </div>
        <div class="status-item">
          <label>最优路径:</label>
          <span class="status-value">{{ optimalPathCount }} 个点</span>
        </div>
        <div class="status-item">
          <label>碰撞检测:</label>
          <span :class="['status-badge', collisionStatus.class]">
            {{ collisionStatus.text }}
          </span>
        </div>
        <div class="status-item">
          <label>重规划次数:</label>
          <span class="status-value">{{ replanCount }} 次</span>
        </div>
      </div>
    </div>

    <!-- 栅格地图设置 -->
    <div class="gridmap-module">
      <h4>🗺️ 栅格地图设置</h4>
      <div class="gridmap-controls">
        <div class="control-group">
          <label>膨胀半径:</label>
          <input 
            type="range" 
            v-model.number="gridMapParams.inflationRadius" 
            min="0.1" 
            max="2.0" 
            step="0.1"
            class="param-slider"
            :disabled="!isConnected"
          >
          <span>{{ gridMapParams.inflationRadius.toFixed(1) }}m</span>
        </div>
        
        <div class="control-group">
          <label>分辨率:</label>
          <input 
            type="range" 
            v-model.number="gridMapParams.resolution" 
            min="0.05" 
            max="0.5" 
            step="0.05"
            class="param-slider"
            :disabled="!isConnected"
          >
          <span>{{ gridMapParams.resolution.toFixed(2) }}m</span>
        </div>
        
        <div class="control-group">
          <label>地图更新频率:</label>
          <select v-model="gridMapParams.updateFrequency" class="control-select">
            <option value="high">高频 (10Hz)</option>
            <option value="medium">中频 (5Hz)</option>
            <option value="low">低频 (2Hz)</option>
            <option value="reduced">降频模式</option>
          </select>
        </div>
        
        <button 
          @click="applyGridMapParams" 
          class="btn btn-primary"
          :disabled="!isConnected"
        >
          💾 应用地图参数
        </button>
      </div>
    </div>

    <!-- 规划参数设置 -->
    <div class="planning-params-module">
      <h4>⚙️ 规划参数设置</h4>
      <div class="planning-params">
        <div class="param-section">
          <h5>轨迹优化参数</h5>
          <div class="param-grid">
            <div class="param-item">
              <label>最大速度:</label>
              <input 
                type="number" 
                v-model.number="planningParams.maxVelocity" 
                step="0.1"
                class="param-input"
                :disabled="!isConnected"
              >
              <span>m/s</span>
            </div>
            <div class="param-item">
              <label>最大加速度:</label>
              <input 
                type="number" 
                v-model.number="planningParams.maxAcceleration" 
                step="0.1"
                class="param-input"
                :disabled="!isConnected"
              >
              <span>m/s²</span>
            </div>
            <div class="param-item">
              <label>安全距离:</label>
              <input 
                type="number" 
                v-model.number="planningParams.safeDistance" 
                step="0.1"
                class="param-input"
                :disabled="!isConnected"
              >
              <span>m</span>
            </div>
          </div>
        </div>
        
        <div class="param-section">
          <h5>优化权重</h5>
          <div class="param-grid">
            <div class="param-item">
              <label>平滑权重:</label>
              <input 
                type="range" 
                v-model.number="planningParams.smoothWeight" 
                min="0" 
                max="1" 
                step="0.1"
                class="param-slider"
                :disabled="!isConnected"
              >
              <span>{{ planningParams.smoothWeight.toFixed(1) }}</span>
            </div>
            <div class="param-item">
              <label>距离权重:</label>
              <input 
                type="range" 
                v-model.number="planningParams.distanceWeight" 
                min="0" 
                max="1" 
                step="0.1"
                class="param-slider"
                :disabled="!isConnected"
              >
              <span>{{ planningParams.distanceWeight.toFixed(1) }}</span>
            </div>
            <div class="param-item">
              <label>时间权重:</label>
              <input 
                type="range" 
                v-model.number="planningParams.timeWeight" 
                min="0" 
                max="1" 
                step="0.1"
                class="param-slider"
                :disabled="!isConnected"
              >
              <span>{{ planningParams.timeWeight.toFixed(1) }}</span>
            </div>
          </div>
        </div>
        
        <button 
          @click="applyPlanningParams" 
          class="btn btn-primary"
          :disabled="!isConnected"
        >
          💾 应用规划参数
        </button>
      </div>
    </div>

    <!-- 规划控制 -->
    <div class="planning-control-module">
      <h4>🎯 规划控制</h4>
      <div class="planning-controls">
        <div class="control-buttons">
          <button 
            @click="startPlanning" 
            class="btn btn-success"
            :disabled="!isConnected"
          >
            🚀 开始规划
          </button>
          <button 
            @click="stopPlanning" 
            class="btn btn-warning"
            :disabled="!isConnected"
          >
            ⏹️ 停止规划
          </button>
          <button 
            @click="replan" 
            class="btn btn-info"
            :disabled="!isConnected"
          >
            🔄 重新规划
          </button>
          <button 
            @click="clearObstacles" 
            class="btn btn-danger"
            :disabled="!isConnected"
          >
            🗑️ 清空障碍物
          </button>
        </div>
        
        <div class="planning-modes">
          <label>规划模式:</label>
          <div class="mode-buttons">
            <button 
              v-for="mode in planningModes" 
              :key="mode.id"
              @click="setPlanningMode(mode.id)"
              class="btn btn-mode"
              :class="{ active: planningMode === mode.id }"
              :disabled="!isConnected"
            >
              {{ mode.icon }} {{ mode.name }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 可视化设置 -->
    <div class="visualization-module">
      <h4>👁️ 可视化设置</h4>
      <div class="visualization-controls">
        <div class="viz-options">
          <div class="viz-option">
            <input 
              type="checkbox" 
              v-model="visualization.showOptimalPath" 
              id="showOptimalPath"
              :disabled="!isConnected"
            >
            <label for="showOptimalPath">显示最优路径</label>
          </div>
          <div class="viz-option">
            <input 
              type="checkbox" 
              v-model="visualization.showInflatedMap" 
              id="showInflatedMap"
              :disabled="!isConnected"
            >
            <label for="showInflatedMap">显示膨胀地图</label>
          </div>
          <div class="viz-option">
            <input 
              type="checkbox" 
              v-model="visualization.showTrajectory" 
              id="showTrajectory"
              :disabled="!isConnected"
            >
            <label for="showTrajectory">显示规划轨迹</label>
          </div>
          <div class="viz-option">
            <input 
              type="checkbox" 
              v-model="visualization.showObstacles" 
              id="showObstacles"
              :disabled="!isConnected"
            >
            <label for="showObstacles">显示障碍物</label>
          </div>
        </div>
        
        <button 
          @click="applyVisualization" 
          class="btn btn-primary"
          :disabled="!isConnected"
        >
          👁️ 应用可视化设置
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EgoPlanner',
  props: {
    isConnected: {
      type: Boolean,
      required: true,
      default: false
    }
  },
  data() {
    return {
      planningStatus: {
        text: '就绪',
        class: 'ready'
      },
      optimalPathCount: 0,
      collisionStatus: {
        text: '安全',
        class: 'safe'
      },
      replanCount: 0,
      
      gridMapParams: {
        inflationRadius: 0.5,
        resolution: 0.1,
        updateFrequency: 'medium'
      },
      
      planningParams: {
        maxVelocity: 2.0,
        maxAcceleration: 1.0,
        safeDistance: 0.3,
        smoothWeight: 0.5,
        distanceWeight: 0.3,
        timeWeight: 0.2
      },
      
      planningMode: 'global',
      planningModes: [
        { id: 'global', icon: '🌍', name: '全局规划' },
        { id: 'local', icon: '📍', name: '局部规划' },
        { id: 'hybrid', icon: '🔄', name: '混合规划' },
        { id: 'emergency', icon: '🚨', name: '紧急规划' }
      ],
      
      visualization: {
        showOptimalPath: true,
        showInflatedMap: true,
        showTrajectory: true,
        showObstacles: true
      }
    };
  },
  methods: {
    applyGridMapParams() {
      this.$emit('apply-gridmap-params', this.gridMapParams);
    },

    applyPlanningParams() {
      this.$emit('apply-planning-params', this.planningParams);
    },

    startPlanning() {
      this.planningStatus = { text: '规划中...', class: 'planning' };
      this.$emit('start-planning');
    },

    stopPlanning() {
      this.planningStatus = { text: '已停止', class: 'stopped' };
      this.$emit('stop-planning');
    },

    replan() {
      this.replanCount++;
      this.$emit('replan');
    },

    clearObstacles() {
      if (confirm('确定要清空所有障碍物信息吗？')) {
        this.$emit('clear-obstacles');
      }
    },

    setPlanningMode(mode) {
      this.planningMode = mode;
      this.$emit('set-planning-mode', mode);
    },

    applyVisualization() {
      this.$emit('apply-visualization', this.visualization);
    }
  },
  emits: [
    'apply-gridmap-params',
    'apply-planning-params',
    'start-planning',
    'stop-planning',
    'replan',
    'clear-obstacles',
    'set-planning-mode',
    'apply-visualization'
  ]
};
</script>

<style scoped>
.planner-status-module, .gridmap-module, .planning-params-module, 
.planning-control-module, .visualization-module {
  margin-bottom: 25px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.planner-status-module h4, .gridmap-module h4, .planning-params-module h4,
.planning-control-module h4, .visualization-module h4 {
  margin: 0 0 15px 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.status-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  background: white;
  border-radius: 6px;
  border-left: 4px solid #e0e0e0;
}

.status-item label {
  font-weight: 600;
  color: #2c3e50;
}

.status-value {
  font-weight: 600;
  color: #409EFF;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  color: white;
}

.status-badge.ready { background: #52c41a; }
.status-badge.planning { background: #faad14; }
.status-badge.stopped { background: #f5222d; }
.status-badge.safe { background: #52c41a; }
.status-badge.warning { background: #faad14; }
.status-badge.danger { background: #f5222d; }

.gridmap-controls, .planning-controls {
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
  min-width: 120px;
}

.param-slider {
  flex: 1;
  min-width: 150px;
}

.control-select {
  padding: 8px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  background: white;
  min-width: 150px;
}

.planning-params {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.param-section {
  background: white;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
}

.param-section h5 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 14px;
}

.param-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
}

.param-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.param-item label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 100px;
  font-size: 14px;
}

.param-input {
  width: 80px;
  padding: 6px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

.control-buttons {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.planning-modes {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.planning-modes label {
  font-weight: 600;
  color: #2c3e50;
  min-width: 80px;
}

.mode-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn-mode {
  background: #f0f0f0;
  color: #666;
  border: 2px solid transparent;
}

.btn-mode.active {
  background: #409EFF;
  color: white;
  border-color: #409EFF;
}

.visualization-controls {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.viz-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 12px;
}

.viz-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.viz-option input[type="checkbox"] {
  width: 16px;
  height: 16px;
}

.viz-option label {
  font-weight: 500;
  color: #2c3e50;
  cursor: pointer;
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

.btn-primary { background: #409EFF; color: white; }
.btn-success { background: #52c41a; color: white; }
.btn-warning { background: #faad14; color: white; }
.btn-danger { background: #f5222d; color: white; }
.btn-info { background: #13c2c2; color: white; }

@media (max-width: 768px) {
  .status-grid {
    grid-template-columns: 1fr;
  }
  
  .control-group {
    flex-direction: column;
    align-items: stretch;
  }
  
  .param-grid {
    grid-template-columns: 1fr;
  }
  
  .control-buttons {
    flex-direction: column;
  }
  
  .planning-modes {
    flex-direction: column;
    align-items: stretch;
  }
  
  .mode-buttons {
    flex-direction: column;
  }
  
  .viz-options {
    grid-template-columns: 1fr;
  }
}
</style>