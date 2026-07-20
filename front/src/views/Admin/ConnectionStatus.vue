<template>
  <div class="control-section component-gap">
    <h3>📡 连接与状态信息</h3>
    <!-- 状态网格布局：分模块展示不同维度状态 -->
    <div class="status-grid">
      <!-- 1. 基础连接状态（ROS+无人机连接） -->
      <div class="status-card">
        <h4>基础连接状态</h4>
        <div class="status-detail">
          <div class="detail-item">
            <label>ROS Bridge连接:</label>
            <span :class="{ 'connected': isConnected }">{{ isConnected ? '✅ 已连接' : '❌ 未连接' }}</span>
          </div>
          <div class="detail-item">
            <label>无人机连接状态:</label>
            <span :class="{ 'connected': uavState.connected }">{{ uavState.connected ? '✅ 已连接' : '❌ 未连接' }}</span>
          </div>
          <div class="detail-item">
            <label>无人机解锁状态:</label>
            <span :class="{ 'armed': uavState.armed }">{{ uavState.armed ? '✅ 已解锁' : '❌ 已锁定' }}</span>
          </div>
          <div class="detail-item">
            <label>当前飞行模式:</label>
            <span>{{ uavState.mode || '未知模式' }}</span>
          </div>
          <div class="detail-item">
            <label>故障安全状态:</label>
            <span :class="{ 'failsafe': uavState.failsafe }">{{ uavState.failsafe ? '🔴 已触发' : '🟢 正常' }}</span>
          </div>
        </div>
      </div>

      <!-- 2. GPS与定位状态 -->
      <div class="status-card">
        <h4>GPS与定位信息</h4>
        <div class="status-detail">
          <div class="detail-item">
            <label>GPS定位状态:</label>
            <span>{{ gpsStatusText }}</span>
          </div>
          <div class="detail-item">
            <label>GPS卫星数量:</label>
            <span>{{ uavState.gpsNum || 0 }} 颗</span>
          </div>
          <div class="detail-item">
            <label>定位源类型:</label>
            <span>{{ locationSourceText }}</span>
          </div>
          <div class="detail-item">
            <label>里程计有效性:</label>
            <span :class="{ 'valid': odomValid }">{{ odomValid ? '✅ 有效' : '❌ 无效' }}</span>
          </div>
          <div class="detail-item">
            <label>相对高度:</label>
            <span>{{ (uavState.relAlt || 0).toFixed(2) }} m</span>
          </div>
        </div>
      </div>

      <!-- 3. 位置与姿态信息 -->
      <div class="status-card">
        <h4>位置与姿态</h4>
        <div class="status-detail">
          <div class="detail-item">
            <label>X坐标 (ENU):</label>
            <span>{{ (uavState.position[0] || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>Y坐标 (ENU):</label>
            <span>{{ (uavState.position[1] || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>Z坐标 (高度):</label>
            <span>{{ (uavState.position[2] || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>偏航角:</label>
            <span>{{ ((uavState.attitude[2] || 0) * 180/Math.PI).toFixed(1) }}°</span>
          </div>
          <div class="detail-item">
            <label>俯仰角:</label>
            <span>{{ ((uavState.attitude[1] || 0) * 180/Math.PI).toFixed(1) }}°</span>
          </div>
          <div class="detail-item">
            <label>横滚角:</label>
            <span>{{ ((uavState.attitude[0] || 0) * 180/Math.PI).toFixed(1) }}°</span>
          </div>
        </div>
      </div>

      <!-- 4. 电池状态 -->
      <div class="status-card">
        <h4>电池状态</h4>
        <div class="status-detail">
          <div class="battery-display">
            <div class="battery-container">
              <div 
                class="battery-level" 
                :style="{ width: batteryPercent + '%' }"
                :class="getBatteryClass(batteryPercent)"
              ></div>
            </div>
            <span class="battery-num">{{ batteryPercent }}%</span>
          </div>
          <div class="detail-item">
            <label>电池电压:</label>
            <span>{{ (uavState.batteryState || 0).toFixed(1) }} V</span>
          </div>
          <div class="detail-item">
            <label>电池状态文本:</label>
            <span>{{ getBatteryText(batteryPercent) }}</span>
          </div>
        </div>
      </div>

      <!-- 5. 相机里程计信息（对应 /uav1/prometheus/camera_odom 话题） -->
      <div class="status-card">
        <h4>相机里程计</h4>
        <div class="status-detail">
          <div class="detail-item">
            <label>相机X坐标:</label>
            <span>{{ (cameraOdom.position.x || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>相机Y坐标:</label>
            <span>{{ (cameraOdom.position.y || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>相机Z坐标:</label>
            <span>{{ (cameraOdom.position.z || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>线性速度X:</label>
            <span>{{ (cameraOdom.linearVel.x || 0).toFixed(2) }} m/s</span>
          </div>
          <div class="detail-item">
            <label>角速度Yaw:</label>
            <span>{{ (cameraOdom.angularVel.z || 0).toFixed(2) }} rad/s</span>
          </div>
        </div>
      </div>

      <!-- 6. 里程计信息（对应 /uav1/prometheus/odom 话题） -->
      <div class="status-card">
        <h4>无人机里程计</h4>
        <div class="status-detail">
          <div class="detail-item">
            <label>里程计X坐标:</label>
            <span>{{ (odom.position.x || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>里程计Y坐标:</label>
            <span>{{ (odom.position.y || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>里程计Z坐标:</label>
            <span>{{ (odom.position.z || 0).toFixed(2) }} m</span>
          </div>
          <div class="detail-item">
            <label>线性速度Y:</label>
            <span>{{ (odom.linearVel.y || 0).toFixed(2) }} m/s</span>
          </div>
          <div class="detail-item">
            <label>角速度Pitch:</label>
            <span>{{ (odom.angularVel.y || 0).toFixed(2) }} rad/s</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConnectionStatus',
  props: {
    // ROS Bridge连接状态（父组件传递）
    isConnected: {
      type: Boolean,
      required: true,
      default: false
    },
    // 无人机状态（来自 /uav1/prometheus/state 话题）
    uavState: {
      type: Object,
      required: true,
      default: () => ({
        connected: false,
        armed: false,
        mode: '未知',
        locationSource: 0,
        odomValid: false,
        gpsStatus: 0,
        gpsNum: 0,
        position: [0, 0, 0],
        attitude: [0, 0, 0],
        batteryState: 0,
        battery_percetage: 0,  // 添加正确的ROS字段名
        batteryPercentage: 0,
        relAlt: 0,
        failsafe: false
      })
    },
    // GPS状态文本（父组件computed传递）
    gpsStatusText: {
      type: String,
      required: true,
      default: '未知'
    },
    // 定位源文本（父组件computed传递）
    locationSourceText: {
      type: String,
      required: true,
      default: '未知'
    },
    // 里程计有效性（父组件传递）
    odomValid: {
      type: Boolean,
      required: true,
      default: false
    },
    // 相机里程计数据（来自 /uav1/prometheus/camera_odom 话题）
    cameraOdom: {
      type: Object,
      required: true,
      default: () => ({
        position: { x: 0, y: 0, z: 0 },
        orientation: { x: 0, y: 0, z: 0, w: 1 },
        linearVel: { x: 0, y: 0, z: 0 },
        angularVel: { x: 0, y: 0, z: 0 }
      })
    },
    // 无人机里程计数据（来自 /uav1/prometheus/odom 话题）
    odom: {
      type: Object,
      required: true,
      default: () => ({
        position: { x: 0, y: 0, z: 0 },
        orientation: { x: 0, y: 0, z: 0, w: 1 },
        linearVel: { x: 0, y: 0, z: 0 },
        angularVel: { x: 0, y: 0, z: 0 }
      })
    }
  },
  computed: {
    // 电池百分比（处理0-1和0-100两种格式）
    batteryPercent() {
      // 优先使用ROS消息中的正确字段名，兼容旧字段名
      const percent = this.uavState.battery_percetage || this.uavState.batteryPercentage || 0;
      console.log('ConnectionStatus电池数据:', {
        raw: percent,
        processed: percent <= 1 ? Math.round(percent * 100) : Math.round(percent),
        uavState: this.uavState
      });
      return percent <= 1 ? Math.round(percent * 100) : Math.round(percent);
    }
  },
  methods: {
    // 电池状态文本映射
    getBatteryText(percent) {
      if (percent <= 0) return '未知状态';
      if (percent > 70) return '良好';
      if (percent > 30) return '一般';
      if (percent > 10) return '低电量（需注意）';
      return '严重低电量（紧急降落）';
    },
    // 电池进度条样式（根据百分比切换颜色）
    getBatteryClass(percent) {
      if (percent <= 0) return 'battery-unknown';
      if (percent > 70) return 'battery-high';
      if (percent > 30) return 'battery-medium';
      return 'battery-low';
    }
  }
};
</script>

<style scoped>
/* 样式保持不变 */
.status-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 15px;
  margin-top: 15px;
}
.status-card {
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #3498db;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}
.status-card h4 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
}
.status-detail {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
}
.detail-item label {
  font-weight: 600;
  color: #7f8c8d;
  white-space: nowrap;
}
.detail-item span {
  color: #2c3e50;
}
.connected {
  color: #27ae60;
  font-weight: bold;
}
.armed {
  color: #e74c3c;
  font-weight: bold;
}
.valid {
  color: #27ae60;
  font-weight: bold;
}
.failsafe {
  color: #e74c3c;
  font-weight: bold;
}
.battery-display {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}
.battery-container {
  flex: 1;
  height: 20px;
  background: #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}
.battery-level {
  height: 100%;
  transition: width 0.5s ease;
  border-radius: 2px;
}
.battery-high { background: #2ecc71; }
.battery-medium { background: #f39c12; }
.battery-low { background: #e74c3c; }
.battery-unknown { background: #95a5a6; }
.battery-num {
  font-weight: bold;
  min-width: 35px;
  text-align: right;
  color: #2c3e50;
}
@media (max-width: 768px) {
  .status-grid {
    grid-template-columns: 1fr;
  }
}
</style>