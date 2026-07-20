<template>
  <div class="control-section component-gap">
    <h3>✈️ 轨迹规划与栅格地图（高德地图版）</h3>
    
    <!-- 高德地图显示区域 -->
    <div class="map-module">
      <h4>高德地图可视化</h4>
      <div class="map-container">
        <div id="amap-container" ref="amapContainer" class="amap-container">
          <!-- 添加加载状态 -->
          <div v-if="!map" class="map-loading">
            <div class="loading-spinner"></div>
            <p>地图加载中...</p>
            <p v-if="mapError" class="error-message">{{ mapError }}</p>
            <button @click="retryLoadMap" class="btn btn-retry">🔄 重试加载</button>
          </div>
        </div>
        
        <!-- 地图控制工具栏 -->
        <div class="map-controls">
          <button 
            @click="centerMap" 
            class="btn btn-map-control"
          >
            🎯 居中地图
          </button>
          <button 
            @click="toggleGridOverlay" 
            class="btn btn-map-control"
            :class="{ 'btn-active': showGridOverlay }"
          >
            {{ showGridOverlay ? '📏 隐藏网格' : '📏 显示网格' }}
          </button>
          <button 
            @click="clearAllWaypoints" 
            class="btn btn-map-control btn-danger"
            :disabled="localTrajectory.poses.length === 0"
          >
            🗑️ 清空路径
          </button>
          <button 
            @click="refreshMap" 
            class="btn btn-map-control"
          >
            🔄 刷新地图
          </button>
          <button 
            @click="toggleObstacleMode" 
            class="btn btn-map-control"
            :class="{ 'btn-active': obstacleMode }"
          >
            {{ obstacleMode ? '🚧 障碍物模式(开)' : '🚧 障碍物模式(关)' }}
          </button>
          <button 
            @click="clearAllObstacles" 
            class="btn btn-map-control btn-danger"
            :disabled="obstacleMarkers.length === 0"
          >
            🗑️ 清空障碍物
          </button>
        </div>
        
        <!-- 地图状态信息 -->
        <div class="map-status">
          <div class="status-item">
            <label>地图中心:</label>
            <span>({{ mapCenter.lng.toFixed(6) }}, {{ mapCenter.lat.toFixed(6) }})</span>
          </div>
          <div class="status-item">
            <label>缩放级别:</label>
            <span>{{ zoomLevel }}</span>
          </div>
          <div class="status-item">
            <label>路径点数量:</label>
            <span>{{ localTrajectory.poses.length }}</span>
          </div>
          <div class="status-item">
            <label>总轨迹长度:</label>
            <span>{{ totalDistance.toFixed(2) }} 米</span>
          </div>
          <div class="status-item">
            <label>障碍物数量:</label>
            <span>{{ obstacleMarkers.length }}</span>
          </div>
          <div class="status-item">
            <label>当前模式:</label>
            <span>{{ obstacleMode ? '障碍物添加' : '路径点添加' }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 路径点管理 -->
    <div class="waypoint-module">
      <h4>路径点设置（{{ localTrajectory.poses.length }} 个点）</h4>
      <div class="waypoint-controls">
        <!-- 添加路径点表单 -->
        <div class="add-waypoint-form">
          <div class="form-fields">
            <div class="form-field">
              <label>经度:</label>
              <input 
                type="number" 
                v-model.number="newWaypoint.lng" 
                class="waypoint-input"
                step="0.000001"
                min="-180"
                max="180"
              >
            </div>
            <div class="form-field">
              <label>纬度:</label>
              <input 
                type="number" 
                v-model.number="newWaypoint.lat" 
                class="waypoint-input"
                step="0.000001"
                min="-90"
                max="90"
              >
            </div>
            <div class="form-field">
              <label>高度 (m):</label>
              <input 
                type="number" 
                v-model.number="newWaypoint.z" 
                class="waypoint-input"
                step="0.1"
                min="0.5"
              >
            </div>
            <button 
              @click="addWaypoint" 
              class="btn btn-add"
              :disabled="!isWaypointValid()"
            >
              ➕ 添加路径点
            </button>
            <button 
              @click="addCurrentPosition" 
              class="btn btn-current"
            >
              📍 当前位置
            </button>
          </div>
        </div>
        
        <!-- 路径点列表 -->
        <div class="waypoint-list" v-if="localTrajectory.poses.length > 0">
          <div class="list-header">
            <div class="col-index">#</div>
            <div class="col-lng">经度</div>
            <div class="col-lat">纬度</div>
            <div class="col-z">高度 (m)</div>
            <div class="col-action">操作</div>
          </div>
          <div 
            v-for="(pose, index) in localTrajectory.poses" 
            :key="index" 
            class="list-row"
            :class="{ 'active': hoveredWaypoint === index }"
            @mouseenter="hoverWaypoint(index)"
            @mouseleave="hoveredWaypoint = -1"
          >
            <div class="col-index">{{ index + 1 }}</div>
            <div class="col-lng">{{ pose.pose.position.lng.toFixed(6) }}</div>
            <div class="col-lat">{{ pose.pose.position.lat.toFixed(6) }}</div>
            <div class="col-z">{{ pose.pose.position.z.toFixed(2) }}</div>
            <div class="col-action">
              <button 
                @click="removeWaypoint(index)" 
                class="btn btn-remove"
              >
                ×
              </button>
              <button 
                @click="centerOnWaypoint(index)" 
                class="btn btn-center"
              >
                🎯
              </button>
              <button 
                @click="editWaypoint(index)" 
                class="btn btn-edit"
              >
                ✏️
              </button>
            </div>
          </div>
        </div>
        <div class="empty-waypoints" v-else>
          🗺️ 暂无路径点，点击地图或使用表单添加路径点...
        </div>
        
        <!-- 路径点批量操作 -->
        <div class="waypoint-batch-actions">
          <button 
            @click="reverseWaypoints" 
            class="btn btn-reverse"
            :disabled="localTrajectory.poses.length < 2"
          >
            ↔️ 反转路径顺序
          </button>
          <button 
            @click="optimizePath" 
            class="btn btn-optimize"
            :disabled="localTrajectory.poses.length < 3"
          >
            🔄 优化路径
          </button>
          <button 
            @click="exportWaypoints" 
            class="btn btn-export"
            :disabled="localTrajectory.poses.length === 0"
          >
            📤 导出路径
          </button>
          <button 
            @click="importWaypoints" 
            class="btn btn-import"
          >
            📥 导入路径
          </button>
          <button 
            @click="generateRandomObstacles" 
            class="btn btn-obstacle"
          >
            🚧 生成障碍物
          </button>
        </div>
      </div>
    </div>

    <!-- 轨迹发送控制 -->
    <div class="trajectory-send-module">
      <div class="send-controls">
        <div class="trajectory-options">
          <label class="option-label">
            <input 
              type="checkbox" 
              v-model="trajectoryOptions.loop"
            >
            循环执行轨迹
          </label>
          <label class="option-label">
            <input 
              type="checkbox" 
              v-model="trajectoryOptions.velocity_control"
            >
            速度控制模式
          </label>
          <div class="form-field">
            <label>轨迹速度 (m/s):</label>
            <input 
              type="number" 
              v-model.number="trajectoryOptions.velocity" 
              class="waypoint-input"
              step="0.1"
              min="0.1"
              max="10"
            >
          </div>
          <div class="form-field">
            <label>安全高度 (m):</label>
            <input 
              type="number" 
              v-model.number="trajectoryOptions.safe_altitude" 
              class="waypoint-input"
              step="0.5"
              min="1.0"
              max="100"
            >
          </div>
        </div>
        
        <div class="trajectory-actions">
          <button 
            @click="publishTrajectory" 
            class="btn btn-publish"
            :disabled="localTrajectory.poses.length < 2"
          >
            📤 发布轨迹
          </button>
          <button 
            @click="executeTrajectory" 
            class="btn btn-execute"
            :disabled="localTrajectory.poses.length < 2"
          >
            🚀 执行轨迹
          </button>
          <button 
            @click="stopTrajectory" 
            class="btn btn-stop"
          >
            ⏹️ 停止轨迹
          </button>
          <button 
            @click="simulateUAV" 
            class="btn btn-simulate"
            :disabled="localTrajectory.poses.length < 2"
          >
            🚁 模拟飞行
          </button>
          <button 
            @click="checkCollision" 
            class="btn btn-check"
            :disabled="localTrajectory.poses.length < 2"
          >
            🛡️ 碰撞检测
          </button>
        </div>
      </div>
    </div>

    <!-- 轨迹执行状态 -->
    <div class="execution-status">
      <h4>轨迹执行状态</h4>
      <div class="status-info">
        <div class="status-item">
          <label>轨迹状态:</label>
          <span :class="getStatusClass(trajectoryStatus)">
            {{ getStatusText(trajectoryStatus) }}
          </span>
        </div>
        <div class="status-item">
          <label>已发布路径点:</label>
          <span>{{ publishedWaypoints }}</span>
        </div>
        <div class="status-item">
          <label>执行进度:</label>
          <span>{{ executionProgress }}%</span>
        </div>
        <div class="status-item">
          <label>模拟位置:</label>
          <span v-if="currentPosition">
            ({{ currentPosition.lng.toFixed(6) }}, {{ currentPosition.lat.toFixed(6) }})
          </span>
          <span v-else>未开始</span>
        </div>
        <div class="status-item">
          <label>碰撞检测:</label>
          <span :class="collisionStatus.class">{{ collisionStatus.text }}</span>
        </div>
      </div>
      
      <!-- 进度条 -->
      <div class="progress-bar" v-if="trajectoryStatus === 'executing'">
        <div 
          class="progress-fill" 
          :style="{ width: executionProgress + '%' }"
        ></div>
      </div>
    </div>

    <!-- 隐藏的文件输入框用于导入 -->
    <input 
      type="file" 
      ref="fileInput" 
      @change="handleFileImport" 
      accept=".json" 
      style="display: none"
    >

    <!-- 编辑路径点模态框 -->
    <div v-if="editingWaypoint !== null" class="modal-overlay">
      <div class="modal-content">
        <h3>编辑路径点 {{ editingWaypoint + 1 }}</h3>
        <div class="form-fields">
          <div class="form-field">
            <label>经度:</label>
            <input 
              type="number" 
              v-model.number="editWaypointData.lng" 
              class="waypoint-input"
              step="0.000001"
            >
          </div>
          <div class="form-field">
            <label>纬度:</label>
            <input 
              type="number" 
              v-model.number="editWaypointData.lat" 
              class="waypoint-input"
              step="0.000001"
            >
          </div>
          <div class="form-field">
            <label>高度 (m):</label>
            <input 
              type="number" 
              v-model.number="editWaypointData.z" 
              class="waypoint-input"
              step="0.1"
              min="0.5"
            >
          </div>
        </div>
        <div class="modal-actions">
          <button @click="saveWaypointEdit" class="btn btn-save">💾 保存</button>
          <button @click="cancelWaypointEdit" class="btn btn-cancel">❌ 取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/* global AMap:readonly */

export default {
  name: 'TrajectoryPlanning',
  props: {
    isConnected: {
      type: Boolean,
      required: true,
      default: false
    },
    trajectory: {
      type: Object,
      required: true,
      default: () => ({
        header: { 
          seq: 0,
          stamp: { secs: 0, nsecs: 0 },
          frame_id: 'world' 
        },
        poses: []
      })
    }
  },
  data() {
    return {
      // 高德地图实例 - 修改为中心为西安邮电大学长安校区
      map: null,
      mapCenter: { lng: 108.90217, lat: 34.154305 }, // 西安邮电大学长安校区坐标
      zoomLevel: 16,
      showGridOverlay: true,
      obstacleMode: false,
      mapError: null,
      mapLoading: false,
      
      // 地图覆盖物
      waypointMarkers: [],
      trajectoryPolyline: null,
      uavMarker: null,
      gridLayer: null,
      obstacleMarkers: [],
      
      // 本地轨迹数据副本，避免直接修改 prop
      localTrajectory: {
        header: { 
          seq: 0,
          stamp: { secs: 0, nsecs: 0 },
          frame_id: 'world' 
        },
        poses: []
      },
      
      hoveredWaypoint: -1,
      editingWaypoint: null,
      editWaypointData: { lng: 0, lat: 0, z: 1.5 },
      
      // 新路径点数据 - 修改为西安邮电大学长安校区坐标
      newWaypoint: {
        lng: 108.90217, // 西安邮电大学长安校区经度
        lat: 34.154305,  // 西安邮电大学长安校区纬度
        z: 1.5
      },
      
      // 轨迹选项
      trajectoryOptions: {
        loop: false,
        velocity_control: false,
        velocity: 1.0,
        safe_altitude: 2.0
      },
      
      // 执行状态
      trajectoryStatus: 'idle', // idle, published, executing, completed, stopped
      publishedWaypoints: 0,
      executionProgress: 0,
      totalDistance: 0,
      collisionStatus: { class: 'safe', text: '安全' },
      
      // 模拟相关
      currentPosition: null,
      simulationInterval: null,
      currentWaypointIndex: 0
    };
  },
  watch: {
    // 监听 prop 变化，同步到本地数据
    trajectory: {
      handler(newVal) {
        this.localTrajectory = JSON.parse(JSON.stringify(newVal));
        this.updateMapDisplay();
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.loadAMapScript();
  },
  beforeUnmount() {
    this.destroyMap();
    this.stopSimulation();
  },
  methods: {
    // 动态加载高德地图脚本
    loadAMapScript() {
      return new Promise((resolve, reject) => {
        this.mapLoading = true;
        this.mapError = null;
        
        if (typeof window.AMap !== 'undefined') {
          this.initGaodeMap().then(resolve).catch(reject);
          return;
        }

        // 设置安全配置（如果需要）
        window._AMapSecurityConfig = {
          securityJsCode: '' // 可选，如果需要请配置
        };

        const script = document.createElement('script');
        // 注意：需要替换为实际的高德地图API Key
        script.src = 'https://webapi.amap.com/maps?v=2.0&key=a44205d01cc641f4bdf0dfd61aec64af&plugin=AMap.MouseTool,AMap.PolyEditor';
        script.async = true;
        
        let loadTimeout = setTimeout(() => {
          console.error('高德地图API加载超时');
          this.mapError = '高德地图加载超时，请检查网络连接';
          this.mapLoading = false;
          this.$emit('log-message', '❌ 高德地图加载超时');
          reject(new Error('加载超时'));
        }, 10000);

        script.onload = () => {
          clearTimeout(loadTimeout);
          console.log('高德地图API加载成功');
          this.$emit('log-message', '✅ 高德地图API加载成功');
          this.initGaodeMap().then(() => {
            this.mapLoading = false;
            resolve();
          }).catch(error => {
            this.mapLoading = false;
            reject(error);
          });
        };
        
        script.onerror = () => {
          clearTimeout(loadTimeout);
          console.error('高德地图API加载失败');
          this.mapError = '高德地图API加载失败，请检查API Key和网络连接';
          this.mapLoading = false;
          this.$emit('log-message', '❌ 高德地图API加载失败');
          reject(new Error('高德地图API加载失败'));
        };
        
        document.head.appendChild(script);
      });
    },

    // 重试加载地图
    retryLoadMap() {
      this.destroyMap();
      this.loadAMapScript().catch(error => {
        console.error('重试加载地图失败:', error);
      });
    },

    // 初始化高德地图
    initGaodeMap() {
      return new Promise((resolve, reject) => {
        // 确保AMap已加载
        if (typeof window.AMap === 'undefined') {
          const error = '高德地图API未加载';
          console.error(error);
          this.mapError = error;
          this.$emit('log-message', '❌ ' + error);
          reject(new Error(error));
          return;
        }

        try {
          // 检查容器是否存在
          const container = document.getElementById('amap-container');
          if (!container) {
            const error = '地图容器未找到';
            console.error(error);
            this.mapError = error;
            this.$emit('log-message', '❌ ' + error);
            reject(new Error(error));
            return;
          }

          // 确保容器可见且有尺寸
          if (container.offsetHeight === 0) {
            console.warn('地图容器高度为0，尝试设置最小高度');
            container.style.minHeight = '500px';
          }

          this.map = new window.AMap.Map('amap-container', {
            zoom: this.zoomLevel,
            center: [this.mapCenter.lng, this.mapCenter.lat],
            viewMode: '3D',
            mapStyle: 'amap://styles/normal',
            pitch: 45,
            features: ['bg', 'road', 'building', 'point']
          });

          // 添加地图加载成功事件
          this.map.on('complete', () => {
            console.log('高德地图初始化完成');
            this.$emit('log-message', '🗺️ 高德地图初始化完成');
            this.setCurrentPositionToCenter();
            resolve();
          });

          // 添加地图加载失败事件
          this.map.on('error', (error) => {
            console.error('地图加载错误:', error);
            this.mapError = '地图加载错误: ' + error;
            this.$emit('log-message', '❌ 地图加载错误');
            reject(error);
          });

          // 添加控件
          this.addMapControls();
          
          // 绑定事件
          this.bindMapEvents();
          
          // 初始化图层
          this.initGridLayer();
          
          // 设置超时，防止complete事件不触发
          setTimeout(() => {
            if (this.map) {
              resolve();
            }
          }, 3000);

        } catch (error) {
          console.error('高德地图初始化失败:', error);
          this.mapError = '高德地图初始化失败: ' + error.message;
          this.$emit('log-message', '❌ 高德地图初始化失败');
          reject(error);
        }
      });
    },

    // 添加地图控件
    addMapControls() {
      if (!this.map || typeof window.AMap === 'undefined') return;
      
      try {
        this.map.addControl(new window.AMap.Scale());
        this.map.addControl(new window.AMap.ToolBar({
          position: 'LT'
        }));
        this.map.addControl(new window.AMap.HawkEye({
          position: 'RB'
        }));
        this.map.addControl(new window.AMap.MapType({
          defaultType: 0,
          position: 'RT'
        }));
      } catch (error) {
        console.warn('添加地图控件失败:', error);
      }
    },

    // 绑定地图事件
    bindMapEvents() {
      if (!this.map) return;
      
      this.map.on('click', this.handleMapClick);
      this.map.on('moveend', this.handleMapMoveEnd);
      this.map.on('zoomchange', this.handleZoomChange);
    },

    // 初始化网格图层
    initGridLayer() {
      if (!this.map || typeof window.AMap === 'undefined') return;
      
      try {
        // 创建自定义网格图层
        this.gridLayer = new window.AMap.CustomLayer(null, {
          zIndex: 100,
          render: this.renderGrid.bind(this)
        });
        
        this.map.add(this.gridLayer);
        this.gridLayer.reRender();
      } catch (error) {
        console.warn('初始化网格图层失败:', error);
      }
    },

    // 渲染网格
    renderGrid() {
      if (!this.map || !this.showGridOverlay || typeof window.AMap === 'undefined') return;
      
      const container = this.map.getContainer();
      const canvas = container.querySelector('canvas');
      if (!canvas) return;
      
      const ctx = canvas.getContext('2d');
      const bounds = this.map.getBounds();
      const zoom = this.map.getZoom();
      
      // 根据缩放级别调整网格密度
      const gridSize = this.calculateGridSize(zoom);
      
      // 使用更明显的网格颜色
      ctx.strokeStyle = 'rgba(0, 100, 255, 0.4)';
      ctx.lineWidth = 1;
      ctx.setLineDash([5, 3]); // 虚线样式
      
      const ne = bounds.getNorthEast();
      const sw = bounds.getSouthWest();
      
      // 绘制经线
      for (let lng = Math.floor(sw.lng / gridSize) * gridSize; lng <= ne.lng; lng += gridSize) {
        const path = [
          new window.AMap.LngLat(lng, sw.lat),
          new window.AMap.LngLat(lng, ne.lat)
        ];
        this.drawLineOnMap(ctx, path);
      }
      
      // 绘制纬线
      for (let lat = Math.floor(sw.lat / gridSize) * gridSize; lat <= ne.lat; lat += gridSize) {
        const path = [
          new window.AMap.LngLat(sw.lng, lat),
          new window.AMap.LngLat(ne.lng, lat)
        ];
        this.drawLineOnMap(ctx, path);
      }
      
      ctx.setLineDash([]); // 重置为实线
    },
    
    // 计算网格大小
    calculateGridSize(zoom) {
      if (zoom > 15) return 0.0001;    // 约10米
      if (zoom > 12) return 0.0005;    // 约50米
      if (zoom > 10) return 0.001;     // 约100米
      if (zoom > 8) return 0.002;      // 约200米
      return 0.005;                    // 约500米
    },
    
    // 在地图上绘制线
    drawLineOnMap(ctx, path) {
      const pixels = path.map(coord => this.map.lngLatToContainer(coord));
      
      ctx.beginPath();
      ctx.moveTo(pixels[0].x, pixels[0].y);
      ctx.lineTo(pixels[1].x, pixels[1].y);
      ctx.stroke();
    },
    
    // 处理地图点击事件
    handleMapClick(event) {
      const lnglat = event.lnglat;
      
      if (this.obstacleMode) {
        // 障碍物模式：添加障碍物
        this.addObstacle(lnglat.lng, lnglat.lat);
      } else {
        // 正常模式：添加路径点
        this.newWaypoint.lng = Number(lnglat.lng.toFixed(6));
        this.newWaypoint.lat = Number(lnglat.lat.toFixed(6));
        this.addWaypoint();
      }
    },
    
    // 处理地图移动结束
    handleMapMoveEnd() {
      const center = this.map.getCenter();
      this.mapCenter.lng = center.lng;
      this.mapCenter.lat = center.lat;
      this.zoomLevel = this.map.getZoom();
    },
    
    // 处理缩放变化
    handleZoomChange() {
      this.zoomLevel = this.map.getZoom();
      // 刷新网格显示
      if (this.gridLayer) {
        this.gridLayer.reRender();
      }
    },
    
    // 添加路径点标记
    addWaypointMarker(pose, index) {
      if (!this.map || typeof window.AMap === 'undefined') {
        console.warn('地图未初始化，无法添加标记');
        return null;
      }
      
      try {
        const marker = new window.AMap.Marker({
          position: [pose.pose.position.lng, pose.pose.position.lat],
          content: this.createMarkerContent(index + 1),
          offset: new window.AMap.Pixel(-12, -12),
          title: `路径点 ${index + 1}`,
          zIndex: 100 + index
        });
        
        marker.on('click', () => {
          this.centerOnWaypoint(index);
        });
        
        marker.on('mouseover', () => {
          this.hoverWaypoint(index);
        });
        
        marker.on('mouseout', () => {
          this.hoveredWaypoint = -1;
        });
        
        this.map.add(marker);
        return marker;
      } catch (error) {
        console.error('添加路径点标记失败:', error);
        return null;
      }
    },
    
    // 创建标记点内容
    createMarkerContent(number) {
      const color = '#2ecc71';
      return `
        <div style="
          width: 24px; 
          height: 24px; 
          background: ${color}; 
          border: 2px solid white; 
          border-radius: 50%; 
          display: flex; 
          align-items: center; 
          justify-content: center; 
          color: white; 
          font-size: 12px; 
          font-weight: bold;
          box-shadow: 0 2px 4px rgba(0,0,0,0.3);
          cursor: pointer;
        ">
          ${number}
        </div>
      `;
    },
    
    // 绘制轨迹线
    updateTrajectoryLine() {
      // 安全移除旧的轨迹线
      if (this.trajectoryPolyline && this.map) {
        try {
          this.map.remove(this.trajectoryPolyline);
        } catch (error) {
          console.warn('移除轨迹线失败:', error);
        }
        this.trajectoryPolyline = null;
      }
      
      if (this.localTrajectory.poses.length < 2 || typeof window.AMap === 'undefined') return;
      
      try {
        const path = this.localTrajectory.poses.map(pose => [
          pose.pose.position.lng,
          pose.pose.position.lat
        ]);
        
        this.trajectoryPolyline = new window.AMap.Polyline({
          path: path,
          strokeColor: this.trajectoryStatus === 'executing' ? '#f39c12' : '#3498db',
          strokeWeight: 4,
          strokeOpacity: 0.8,
          strokeStyle: this.trajectoryStatus === 'executing' ? 'dashed' : 'solid',
          zIndex: 50
        });
        
        this.map.add(this.trajectoryPolyline);
        this.calculateTotalDistance();
      } catch (error) {
        console.error('更新轨迹线失败:', error);
      }
    },
    
    // 计算总距离
    calculateTotalDistance() {
      this.totalDistance = 0;
      for (let i = 1; i < this.localTrajectory.poses.length; i++) {
        const prev = this.localTrajectory.poses[i-1].pose.position;
        const curr = this.localTrajectory.poses[i].pose.position;
        this.totalDistance += this.calculateDistance(prev, curr);
      }
    },
    
    // 计算两点间距离（米）
    calculateDistance(point1, point2) {
      const R = 6371000; // 地球半径（米）
      const dLat = (point2.lat - point1.lat) * Math.PI / 180;
      const dLng = (point2.lng - point1.lng) * Math.PI / 180;
      const a = 
        Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(point1.lat * Math.PI / 180) * Math.cos(point2.lat * Math.PI / 180) *
        Math.sin(dLng/2) * Math.sin(dLng/2);
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      return R * c;
    },
    
    // 设置当前位置到地图中心
    setCurrentPositionToCenter() {
      if (!this.map) {
        console.warn('地图未初始化，无法设置当前位置');
        return;
      }
      
      try {
        const center = this.map.getCenter();
        if (center) {
          this.currentPosition = {
            lng: center.lng,
            lat: center.lat,
            z: 1.5
          };
          this.createUAVMarker();
        }
      } catch (error) {
        console.error('获取地图中心失败:', error);
        // 使用默认位置作为后备
        this.currentPosition = {
          lng: this.mapCenter.lng,
          lat: this.mapCenter.lat,
          z: 1.5
        };
        this.createUAVMarker();
      }
    },
    
    // 创建无人机标记
    createUAVMarker() {
      if (!this.map || typeof window.AMap === 'undefined' || !this.currentPosition) {
        console.warn('条件不满足，无法创建无人机标记');
        return;
      }
      
      try {
        // 移除旧的无人机标记
        if (this.uavMarker && this.map) {
          try {
            this.map.remove(this.uavMarker);
          } catch (error) {
            console.warn('移除旧无人机标记失败:', error);
          }
        }
        
        this.uavMarker = new window.AMap.Marker({
          position: [this.currentPosition.lng, this.currentPosition.lat],
          content: this.createUAVMarkerContent(),
          offset: new window.AMap.Pixel(-15, -15),
          zIndex: 200
        });
        
        this.map.add(this.uavMarker);
      } catch (error) {
        console.error('创建无人机标记失败:', error);
      }
    },
    
    // 创建无人机标记内容
    createUAVMarkerContent() {
      return `
        <div style="
          width: 30px; 
          height: 30px; 
          background: #9b59b6; 
          border: 2px solid white; 
          border-radius: 50%; 
          display: flex; 
          align-items: center; 
          justify-content: center; 
          color: white; 
          font-size: 16px;
          box-shadow: 0 2px 6px rgba(0,0,0,0.4);
          animation: pulse 2s infinite;
        ">
          🚁
        </div>
        <style>
          @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.1); }
            100% { transform: scale(1); }
          }
        </style>
      `;
    },
    
    // 更新无人机位置
    updateUAVPosition(lng, lat) {
      this.currentPosition.lng = lng;
      this.currentPosition.lat = lat;
      
      if (this.uavMarker) {
        try {
          this.uavMarker.setPosition([lng, lat]);
        } catch (error) {
          console.warn('更新无人机位置失败:', error);
        }
      }
    },
    
    // 路径点悬停
    hoverWaypoint(index) {
      this.hoveredWaypoint = index;
    },
    
    // 地图控制方法
    centerMap() {
      if (this.map) {
        // 修改为中心为西安邮电大学长安校区
        this.map.setZoomAndCenter(16, [108.90217, 34.154305]);
        this.$emit('log-message', '🎯 地图已居中到西安邮电大学长安校区');
      }
    },
    
    // 网格显示切换
    toggleGridOverlay() {
      this.showGridOverlay = !this.showGridOverlay;
      if (this.gridLayer) {
        this.gridLayer.reRender();
      }
      this.$emit('log-message', this.showGridOverlay ? '📏 显示网格覆盖' : '📏 隐藏网格覆盖');
    },
    
    // 障碍物模式切换
    toggleObstacleMode() {
      this.obstacleMode = !this.obstacleMode;
      // 切换地图点击事件的行为
      if (this.map) {
        if (this.obstacleMode) {
          this.map.setDefaultCursor('crosshair');
          this.$emit('log-message', '🚧 已进入障碍物添加模式 - 点击地图添加障碍物');
        } else {
          this.map.setDefaultCursor('default');
          this.$emit('log-message', '✅ 已退出障碍物添加模式 - 点击地图添加路径点');
        }
      }
    },
    
    // 刷新地图显示
    refreshMap() {
      if (this.map) {
        this.map.setFitView();
        if (this.gridLayer) {
          this.gridLayer.reRender();
        }
        this.$emit('log-message', '🔄 刷新地图显示');
      }
    },
    
    centerOnWaypoint(index) {
      if (index >= 0 && index < this.localTrajectory.poses.length && this.map) {
        const pose = this.localTrajectory.poses[index];
        this.map.setCenter([pose.pose.position.lng, pose.pose.position.lat]);
        this.map.setZoom(17);
        this.$emit('log-message', `🎯 居中到路径点 ${index + 1}`);
      }
    },
    
    // 验证路径点
    isWaypointValid() {
      return (
        !isNaN(this.newWaypoint.lng) &&
        !isNaN(this.newWaypoint.lat) &&
        !isNaN(this.newWaypoint.z) &&
        this.newWaypoint.z >= 0.5 &&
        this.newWaypoint.lng >= -180 &&
        this.newWaypoint.lng <= 180 &&
        this.newWaypoint.lat >= -90 &&
        this.newWaypoint.lat <= 90
      );
    },
    
    // 添加路径点
    addWaypoint() {
      if (!this.isWaypointValid()) {
        alert('路径点无效！请检查坐标值（经度:-180~180, 纬度:-90~90, 高度≥0.5m）');
        return;
      }
      
      const newPose = {
        header: { frame_id: 'world' },
        pose: {
          position: {
            lng: Number(this.newWaypoint.lng.toFixed(6)),
            lat: Number(this.newWaypoint.lat.toFixed(6)),
            z: Number(this.newWaypoint.z.toFixed(2))
          },
          orientation: { x: 0, y: 0, z: 0, w: 1 }
        }
      };
      
      const updatedPoses = [...this.localTrajectory.poses, newPose];
      
      // 直接更新本地轨迹数据，避免递归调用
      this.localTrajectory.poses = updatedPoses;
      
      // 手动更新地图显示
      this.updateMapDisplay();
      
      // 触发事件通知父组件
      this.$emit('update:trajectory', {
        ...this.localTrajectory,
        poses: [...updatedPoses]
      });
      
      // 自动更新下一个点的默认坐标（稍微偏移）
      this.newWaypoint.lng += 0.0001;
      this.newWaypoint.lat += 0.0001;
      
      this.$emit('log-message', `📍 添加路径点 ${updatedPoses.length}: (${newPose.pose.position.lng.toFixed(6)}, ${newPose.pose.position.lat.toFixed(6)})`);
    },
    
    addCurrentPosition() {
      // 如果当前没有位置，使用地图中心
      if (!this.currentPosition) {
        if (this.map) {
          try {
            const center = this.map.getCenter();
            this.currentPosition = {
              lng: center.lng,
              lat: center.lat,
              z: 1.5
            };
          } catch (error) {
            console.error('获取地图中心失败，使用默认位置:', error);
            this.currentPosition = {
              lng: this.mapCenter.lng,
              lat: this.mapCenter.lat,
              z: 1.5
            };
          }
        } else {
          // 如果地图也未初始化，使用默认位置
          this.currentPosition = {
            lng: this.mapCenter.lng,
            lat: this.mapCenter.lat,
            z: 1.5
          };
        }
      }
      
      this.newWaypoint.lng = Number(this.currentPosition.lng.toFixed(6));
      this.newWaypoint.lat = Number(this.currentPosition.lat.toFixed(6));
      this.newWaypoint.z = Number(Math.max(this.currentPosition.z, 1.5).toFixed(2));
      
      this.addWaypoint();
    },
    
    // 编辑路径点
    editWaypoint(index) {
      if (index >= 0 && index < this.localTrajectory.poses.length) {
        const pose = this.localTrajectory.poses[index].pose.position;
        this.editingWaypoint = index;
        this.editWaypointData = {
          lng: pose.lng,
          lat: pose.lat,
          z: pose.z
        };
      }
    },
    
    saveWaypointEdit() {
      if (this.editingWaypoint !== null) {
        const updatedPoses = [...this.localTrajectory.poses];
        updatedPoses[this.editingWaypoint].pose.position = { ...this.editWaypointData };
        this.updateTrajectory(updatedPoses);
        this.$emit('log-message', `✏️ 更新路径点 ${this.editingWaypoint + 1}`);
      }
      this.cancelWaypointEdit();
    },
    
    cancelWaypointEdit() {
      this.editingWaypoint = null;
      this.editWaypointData = { lng: 0, lat: 0, z: 1.5 };
    },
    
    removeWaypoint(index) {
      if (index < 0 || index >= this.waypointMarkers.length) return;
      
      // 安全移除地图标记
      const marker = this.waypointMarkers[index];
      if (marker && this.map) {
        try {
          this.map.remove(marker);
        } catch (error) {
          console.warn('移除路径点标记失败:', error);
        }
      }
      
      // 从数组中移除
      this.waypointMarkers.splice(index, 1);
      
      const updatedPoses = this.localTrajectory.poses.filter((_, i) => i !== index);
      this.updateTrajectory(updatedPoses);
      
      this.$emit('log-message', `🗑️ 删除路径点 ${index + 1}`);
    },
    
    // 安全清除所有路径点标记
    clearWaypointMarkers() {
      if (!this.map || !this.waypointMarkers.length) return;
      
      // 使用过滤方式安全移除标记
      this.waypointMarkers = this.waypointMarkers.filter(marker => {
        if (marker && typeof marker.remove === 'function') {
          try {
            this.map.remove(marker);
            return false; // 从数组中移除
          } catch (error) {
            console.warn('移除标记失败:', error);
            return false;
          }
        }
        return false;
      });
    },
    
    clearAllWaypoints() {
      if (confirm('确定要清空所有路径点吗？')) {
        // 安全清除所有标记
        this.clearWaypointMarkers();
        
        // 使用本地数据
        const updatedPoses = [];
        this.localTrajectory.poses = updatedPoses;
        
        // 手动更新地图显示
        this.updateMapDisplay();
        
        // 触发事件通知父组件
        this.$emit('update:trajectory', {
          ...this.localTrajectory,
          poses: [...updatedPoses]
        });
        
        this.trajectoryStatus = 'idle';
        this.publishedWaypoints = 0;
        this.executionProgress = 0;
        this.totalDistance = 0;
        
        // 安全清除轨迹线
        if (this.trajectoryPolyline && this.map) {
          try {
            this.map.remove(this.trajectoryPolyline);
          } catch (error) {
            console.warn('移除轨迹线失败:', error);
          }
          this.trajectoryPolyline = null;
        }
        
        this.$emit('log-message', '🗑️ 清空所有路径点');
      }
    },
    
    // 更新轨迹数据（修复：不直接修改 prop）
    updateTrajectory(poses) {
      // 直接更新本地数据，不触发 watch
      this.localTrajectory.poses = poses;
      
      // 手动更新地图显示
      this.updateMapDisplay();
      
      // 触发事件通知父组件
      this.$emit('update:trajectory', {
        ...this.localTrajectory,
        poses: [...poses]
      });
    },
    
    // 更新地图显示
    updateMapDisplay() {
      // 安全地清除所有标记
      this.clearWaypointMarkers();
      
      // 重新添加标记
      this.localTrajectory.poses.forEach((pose, index) => {
        const marker = this.addWaypointMarker(pose, index);
        if (marker) {
          this.waypointMarkers.push(marker);
        }
      });
      
      // 更新轨迹线
      this.updateTrajectoryLine();
    },
    
    reverseWaypoints() {
      const reversedPoses = [...this.localTrajectory.poses].reverse();
      this.updateTrajectory(reversedPoses);
      this.$emit('log-message', '↔️ 反转路径顺序');
    },
    
    optimizePath() {
      if (this.localTrajectory.poses.length < 3) return;
      
      // 简单的路径优化：按经度排序（实际应用中可以使用更复杂的算法）
      const optimized = [...this.localTrajectory.poses].sort((a, b) => 
        a.pose.position.lng - b.pose.position.lng
      );
      this.updateTrajectory(optimized);
      
      this.$emit('log-message', '🔄 路径已优化');
    },
    
    exportWaypoints() {
      const waypoints = this.localTrajectory.poses.map(pose => ({
        lng: pose.pose.position.lng,
        lat: pose.pose.position.lat,
        z: pose.pose.position.z
      }));
      
      const dataStr = JSON.stringify(waypoints, null, 2);
      const dataBlob = new Blob([dataStr], { type: 'application/json' });
      
      const link = document.createElement('a');
      link.href = URL.createObjectURL(dataBlob);
      link.download = `trajectory_${new Date().toISOString().split('T')[0]}.json`;
      link.click();
      
      this.$emit('log-message', '📤 导出轨迹数据');
    },
    
    importWaypoints() {
      this.$refs.fileInput.click();
    },
    
    handleFileImport(event) {
      const file = event.target.files[0];
      if (!file) return;
      
      const reader = new FileReader();
      reader.onload = (e) => {
        try {
          const waypoints = JSON.parse(e.target.result);
          if (Array.isArray(waypoints)) {
            const poses = waypoints.map(wp => ({
              header: { frame_id: 'world' },
              pose: {
                position: {
                  lng: wp.lng,
                  lat: wp.lat,
                  z: wp.z || 1.5
                },
                orientation: { x: 0, y: 0, z: 0, w: 1 }
              }
            }));
            this.updateTrajectory(poses);
            this.$emit('log-message', '📥 导入轨迹数据成功');
          }
        } catch (error) {
          alert('文件格式错误！');
          console.error('导入失败:', error);
          this.$emit('log-message', '❌ 导入轨迹数据失败');
        }
      };
      reader.readAsText(file);
      
      // 重置文件输入
      event.target.value = '';
    },
    
    // 障碍物管理
    addObstacle(lng, lat) {
      if (!this.map || typeof window.AMap === 'undefined') return;
      
      try {
        const marker = new window.AMap.Marker({
          position: [lng, lat],
          content: this.createObstacleContent(),
          offset: new window.AMap.Pixel(-8, -8),
          zIndex: 90,
          draggable: true // 允许拖动障碍物
        });
        
        // 添加拖拽事件
        marker.on('dragend', (event) => {
          const newPos = event.target.getPosition();
          this.$emit('log-message', `🚧 移动障碍物到: (${newPos.lng.toFixed(6)}, ${newPos.lat.toFixed(6)})`);
        });
        
        marker.on('click', () => {
          if (confirm('是否删除此障碍物？')) {
            try {
              this.map.remove(marker);
              this.obstacleMarkers = this.obstacleMarkers.filter(m => m !== marker);
              this.$emit('log-message', '🗑️ 删除障碍物');
            } catch (error) {
              console.warn('删除障碍物失败:', error);
            }
          }
        });
        
        this.map.add(marker);
        this.obstacleMarkers.push(marker);
        
        this.$emit('log-message', `🚧 添加障碍物: (${lng.toFixed(6)}, ${lat.toFixed(6)})`);
      } catch (error) {
        console.error('添加障碍物失败:', error);
      }
    },
    
    createObstacleContent() {
      return `
        <div style="
          width: 20px; 
          height: 20px; 
          background: #e74c3c; 
          border: 2px solid white; 
          border-radius: 50%; 
          box-shadow: 0 2px 6px rgba(0,0,0,0.4);
          cursor: pointer;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 12px;
          font-weight: bold;
        ">🚧</div>
      `;
    },
    
    // 清除所有障碍物
    clearAllObstacles() {
      if (this.obstacleMarkers.length === 0) return;
      
      if (confirm(`确定要清除所有 ${this.obstacleMarkers.length} 个障碍物吗？`)) {
        this.obstacleMarkers.forEach(marker => {
          if (marker && this.map) {
            try {
              this.map.remove(marker);
            } catch (error) {
              console.warn('移除障碍物标记失败:', error);
            }
          }
        });
        this.obstacleMarkers = [];
        this.$emit('log-message', '🗑️ 清除所有障碍物');
      }
    },
    
    generateRandomObstacles() {
      if (!this.map) return;
      
      // 在西安邮电大学长安校区周围生成5个随机障碍物
      for (let i = 0; i < 5; i++) {
        const lng = 108.90217 + (Math.random() - 0.5) * 0.002; // 西安邮电大学长安校区周围200米范围内
        const lat = 34.154305 + (Math.random() - 0.5) * 0.002;
        this.addObstacle(lng, lat);
      }
      
      this.$emit('log-message', '🚧 在西安邮电大学长安校区周围生成5个随机障碍物');
    },
    
    // 碰撞检测
    checkCollision() {
      if (this.localTrajectory.poses.length < 2) return;
      
      let hasCollision = false;
      const safeDistance = 30; // 安全距离30米
      
      // 简单碰撞检测：检查路径点是否靠近障碍物
      for (const pose of this.localTrajectory.poses) {
        for (const obstacle of this.obstacleMarkers) {
          if (!obstacle) continue;
          
          try {
            const obstaclePos = obstacle.getPosition();
            const distance = this.calculateDistance(
              pose.pose.position,
              { lng: obstaclePos.lng, lat: obstaclePos.lat }
            );
            
            if (distance < safeDistance) {
              hasCollision = true;
              break;
            }
          } catch (error) {
            console.warn('计算障碍物距离失败:', error);
          }
        }
        if (hasCollision) break;
      }
      
      if (hasCollision) {
        this.collisionStatus = { class: 'danger', text: '⚠️ 检测到碰撞风险' };
        this.$emit('log-message', '⚠️ 碰撞检测：发现路径与障碍物冲突');
      } else {
        this.collisionStatus = { class: 'safe', text: '✅ 路径安全' };
        this.$emit('log-message', '✅ 碰撞检测：路径安全');
      }
    },
    
    // 轨迹控制方法
    publishTrajectory() {
      if (this.localTrajectory.poses.length < 2) {
        alert('至少需要2个路径点才能发布轨迹！');
        return;
      }
      
      const now = new Date();
      const secs = Math.floor(now.getTime() / 1000);
      const nsecs = (now.getTime() % 1000) * 1000000;
      
      const trajectoryToSend = {
        ...this.localTrajectory,
        header: {
          ...this.localTrajectory.header,
          frame_id: 'world',
          stamp: { secs: secs, nsecs: nsecs }
        }
      };
      
      this.trajectoryStatus = 'published';
      this.publishedWaypoints = this.localTrajectory.poses.length;
      
      this.$emit('send-trajectory', trajectoryToSend);
      this.$emit('log-message', `📤 发布轨迹，包含 ${this.publishedWaypoints} 个路径点`);
    },
    
    executeTrajectory() {
      if (this.localTrajectory.poses.length < 2) {
        alert('至少需要2个路径点才能执行轨迹！');
        return;
      }
      
      if (this.trajectoryStatus !== 'published') {
        this.publishTrajectory();
      }
      
      this.trajectoryStatus = 'executing';
      this.executionProgress = 0;
      this.currentWaypointIndex = 0;
      
      this.$emit('log-message', '🚀 开始执行轨迹');
      
      this.simulateExecutionProgress();
    },
    
    stopTrajectory() {
      this.trajectoryStatus = 'stopped';
      this.executionProgress = 0;
      this.stopSimulation();
      
      this.$emit('log-message', '⏹️ 停止轨迹执行');
    },
    
    // 模拟执行进度
    simulateExecutionProgress() {
      if (this.trajectoryStatus !== 'executing') return;
      
      const totalPoints = this.localTrajectory.poses.length;
      const interval = 1000; // 1秒更新一次
      
      this.simulationInterval = setInterval(() => {
        if (this.trajectoryStatus !== 'executing') {
          clearInterval(this.simulationInterval);
          return;
        }
        
        this.executionProgress += 100 / totalPoints;
        this.currentWaypointIndex = Math.floor(this.executionProgress / 100 * totalPoints);
        
        if (this.executionProgress >= 100) {
          this.executionProgress = 100;
          this.trajectoryStatus = 'completed';
          this.$emit('log-message', '✅ 轨迹执行完成');
          clearInterval(this.simulationInterval);
        }
      }, interval);
    },
    
    // 模拟无人机飞行
    simulateUAV() {
      if (this.localTrajectory.poses.length < 2) {
        alert('至少需要2个路径点才能模拟飞行！');
        return;
      }
      
      this.stopSimulation();
      this.trajectoryStatus = 'executing';
      this.executionProgress = 0;
      this.currentWaypointIndex = 0;
      
      const startPose = this.localTrajectory.poses[0].pose.position;
      this.updateUAVPosition(startPose.lng, startPose.lat);
      
      this.animateUAVFlight();
    },
    
    // 动画无人机飞行
    animateUAVFlight() {
      const totalPoints = this.localTrajectory.poses.length;
      let currentSegment = 0;
      let progressInSegment = 0;
      const segmentDuration = 2000; // 每段2秒
      
      const animate = () => {
        if (this.trajectoryStatus !== 'executing') return;
        
        progressInSegment += 16; // 约60fps
        
        if (progressInSegment >= segmentDuration) {
          currentSegment++;
          progressInSegment = 0;
          
          if (currentSegment >= totalPoints - 1) {
            this.trajectoryStatus = 'completed';
            this.executionProgress = 100;
            this.$emit('log-message', '✅ 模拟飞行完成');
            return;
          }
        }
        
        const startPoint = this.localTrajectory.poses[currentSegment].pose.position;
        const endPoint = this.localTrajectory.poses[currentSegment + 1].pose.position;
        
        const t = progressInSegment / segmentDuration;
        const currentLng = startPoint.lng + (endPoint.lng - startPoint.lng) * t;
        const currentLat = startPoint.lat + (endPoint.lat - startPoint.lat) * t;
        
        this.updateUAVPosition(currentLng, currentLat);
        this.executionProgress = ((currentSegment + t) / (totalPoints - 1)) * 100;
        this.currentWaypointIndex = currentSegment;
        
        if (this.trajectoryStatus === 'executing') {
          requestAnimationFrame(animate);
        }
      };
      
      animate();
    },
    
    // 停止模拟
    stopSimulation() {
      if (this.simulationInterval) {
        clearInterval(this.simulationInterval);
        this.simulationInterval = null;
      }
      this.trajectoryStatus = 'stopped';
    },
    
    // 清理资源
    destroyMap() {
      this.stopSimulation();
      
      // 安全清除所有覆盖物
      this.clearWaypointMarkers();
      
      if (this.trajectoryPolyline && this.map) {
        try {
          this.map.remove(this.trajectoryPolyline);
        } catch (error) {
          console.warn('移除轨迹线失败:', error);
        }
        this.trajectoryPolyline = null;
      }
      
      if (this.uavMarker && this.map) {
        try {
          this.map.remove(this.uavMarker);
        } catch (error) {
          console.warn('移除无人机标记失败:', error);
        }
        this.uavMarker = null;
      }
      
      // 安全清除障碍物
      if (this.obstacleMarkers.length && this.map) {
        this.obstacleMarkers.forEach(marker => {
          if (marker) {
            try {
              this.map.remove(marker);
            } catch (error) {
              console.warn('移除障碍物标记失败:', error);
            }
          }
        });
        this.obstacleMarkers = [];
      }
      
      if (this.map) {
        try {
          this.map.destroy();
        } catch (error) {
          console.warn('销毁地图失败:', error);
        }
        this.map = null;
      }
    },
    
    getStatusClass(status) {
      const classes = {
        idle: 'status-idle',
        published: 'status-published',
        executing: 'status-executing',
        completed: 'status-completed',
        stopped: 'status-stopped'
      };
      return classes[status] || 'status-idle';
    },
    
    getStatusText(status) {
      const texts = {
        idle: '待发布',
        published: '已发布',
        executing: '执行中',
        completed: '已完成',
        stopped: '已停止'
      };
      return texts[status] || '未知状态';
    }
  },
  emits: ['send-trajectory', 'update:trajectory', 'log-message']
};
</script>

<style scoped>
/* 样式保持不变 */
.control-section {
  margin-bottom: 25px;
}

.control-section h3 {
  margin: 0 0 20px 0;
  color: #2c3e50;
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.map-module {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.map-module h4 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.map-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.amap-container {
  width: 100%;
  height: 500px;
  border: 2px solid #34495e;
  border-radius: 6px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  position: relative;
}

.map-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: #f8f9fa;
  color: #666;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 10;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e0e0e0;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-message {
  color: #e74c3c;
  margin-top: 10px;
  text-align: center;
}

.btn-retry {
  background: #3498db;
  color: white;
  margin-top: 15px;
}

.map-controls {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.btn-map-control {
  background: #34495e;
  color: white;
  padding: 6px 12px;
  font-size: 12px;
}

.btn-map-control.btn-active {
  background: #3498db;
}

.btn-map-control.btn-danger {
  background: #e74c3c;
}

.map-status {
  display: flex;
  gap: 20px;
  padding: 10px;
  background: white;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  flex-wrap: wrap;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}

.status-item label {
  font-weight: 600;
  color: #7f8c8d;
}

.status-item span {
  color: #2c3e50;
  font-weight: 500;
}

.waypoint-module {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.waypoint-module h4 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.waypoint-controls {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.add-waypoint-form {
  padding: 10px 0;
}

.form-fields {
  display: flex;
  gap: 15px;
  align-items: flex-end;
  flex-wrap: wrap;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 5px;
  min-width: 120px;
}

.form-field label {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.waypoint-input {
  padding: 8px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  color: #2c3e50;
}

.waypoint-list {
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #e0e0e0;
  max-height: 300px;
  overflow-y: auto;
}

.list-header {
  display: flex;
  background: #2c3e50;
  color: white;
  font-weight: bold;
  position: sticky;
  top: 0;
}

.list-row {
  display: flex;
  background: white;
  border-bottom: 1px solid #e0e0e0;
  transition: background 0.3s;
}

.list-row:hover,
.list-row.active {
  background: #f1f2f6;
}

.col-index { width: 50px; padding: 10px; text-align: center; }
.col-lng { flex: 1; padding: 10px; }
.col-lat { flex: 1; padding: 10px; }
.col-z { flex: 1; padding: 10px; }
.col-action { width: 120px; padding: 10px; text-align: center; display: flex; gap: 5px; justify-content: center; }

.empty-waypoints {
  padding: 30px;
  text-align: center;
  color: #7f8c8d;
  font-size: 14px;
  background: white;
  border-radius: 6px;
  border: 1px dashed #e0e0e0;
}

.waypoint-batch-actions {
  display: flex;
  gap: 10px;
  padding: 5px 0;
  flex-wrap: wrap;
}

.trajectory-send-module {
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.send-controls {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.trajectory-options {
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.option-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #2c3e50;
  cursor: pointer;
}

.trajectory-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.execution-status {
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.execution-status h4 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-info {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  margin-bottom: 15px;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #2ecc71, #3498db);
  transition: width 0.3s ease;
}

.status-published {
  color: #3498db;
  font-weight: bold;
}

.status-executing {
  color: #f39c12;
  font-weight: bold;
}

.status-completed {
  color: #27ae60;
  font-weight: bold;
}

.status-stopped {
  color: #e74c3c;
  font-weight: bold;
}

.status-idle {
  color: #7f8c8d;
  font-weight: bold;
}

.safe {
  color: #27ae60;
  font-weight: bold;
}

.danger {
  color: #e74c3c;
  font-weight: bold;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  min-width: 400px;
}

.modal-content h3 {
  margin: 0 0 15px 0;
  color: #2c3e50;
}

.modal-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
  justify-content: flex-end;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.btn-add { background: #2ecc71; color: white; }
.btn-current { background: #9b59b6; color: white; }
.btn-remove { 
  background: #e74c3c; 
  color: white; 
  width: 28px; 
  height: 28px; 
  padding: 0; 
  border-radius: 50%; 
  font-size: 16px; 
  display: flex;
  align-items: center;
  justify-content: center;
}
.btn-center {
  background: #3498db;
  color: white;
  width: 28px;
  height: 28px;
  padding: 0;
  border-radius: 50%;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.btn-edit {
  background: #f39c12;
  color: white;
  width: 28px;
  height: 28px;
  padding: 0;
  border-radius: 50%;
  font-size: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.btn-reverse { background: #f39c12; color: white; }
.btn-optimize { background: #1abc9c; color: white; }
.btn-export { background: #34495e; color: white; }
.btn-import { background: #95a5a6; color: white; }
.btn-obstacle { background: #e67e22; color: white; }
.btn-publish { background: #3498db; color: white; }
.btn-execute { background: #2ecc71; color: white; }
.btn-stop { background: #e74c3c; color: white; }
.btn-simulate { background: #9b59b6; color: white; }
.btn-check { background: #e74c3c; color: white; }
.btn-save { background: #2ecc71; color: white; }
.btn-cancel { background: #95a5a6; color: white; }

@media (max-width: 768px) {
  .amap-container {
    height: 400px;
  }
  
  .form-fields {
    flex-direction: column;
    align-items: stretch;
  }
  
  .list-header, .list-row {
    flex-direction: column;
  }
  
  .col-index, .col-lng, .col-lat, .col-z, .col-action {
    width: auto;
    padding: 8px;
    border-bottom: 1px solid #e0e0e0;
    text-align: left;
  }
  
  .col-action {
    justify-content: flex-start;
  }
  
  .waypoint-batch-actions,
  .trajectory-actions {
    flex-direction: column;
  }
  
  .send-controls {
    flex-direction: column;
  }
  
  .trajectory-options {
    justify-content: center;
  }
  
  .status-info {
    flex-direction: column;
    gap: 10px;
  }
  
  .map-status {
    flex-direction: column;
    gap: 10px;
  }
  
  .map-controls {
    flex-direction: column;
  }
  
  .modal-content {
    min-width: 90%;
    margin: 20px;
  }
}
</style>