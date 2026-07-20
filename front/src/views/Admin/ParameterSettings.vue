<template>
  <div class="control-section component-gap">
    <h3>⚙️ 参数设置与偏移控制</h3>
    
    <!-- 参数设置 -->
    <div class="param-setting-module">
      <h4>无人机参数配置</h4>
      <div class="param-form">
        <div class="param-list">
          <div v-for="(param, index) in paramList" :key="index" class="param-row">
            <input 
              type="text" 
              v-model="param.key" 
              class="param-key"
              placeholder="参数名"
              :disabled="!isConnected"
            >
            <input 
              type="text" 
              v-model="param.value" 
              class="param-value"
              placeholder="参数值"
              :disabled="!isConnected"
            >
            <button 
              @click="removeParam(index)" 
              class="btn btn-remove"
              :disabled="!isConnected || paramList.length <= 1"
            >
              ×
            </button>
          </div>
        </div>
        <div class="param-actions">
          <button 
            @click="addParam" 
            class="btn btn-add"
            :disabled="!isConnected"
          >
            ➕ 添加参数
          </button>
          <button 
            @click="handleSendParams" 
            class="btn btn-send"
            :disabled="!isConnected || !isParamValid()"
          >
            📤 发送参数配置
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ParameterSettings',
  props: {
    isConnected: {
      type: Boolean,
      required: true,
      default: false
    }
  },
  data() {
    return {
      paramList: [
        { key: '', value: '' }
      ]
    };
  },
  methods: {
    addParam() {
      this.paramList.push({ key: '', value: '' });
    },
    removeParam(index) {
      if (this.paramList.length > 1) {
        this.paramList.splice(index, 1);
      }
    },
    isParamValid() {
      return this.paramList.every(param => param.key.trim() !== '' && param.value.trim() !== '');
    },
    handleSendParams() {
      if (!this.isParamValid()) {
        alert('请填写所有参数的"参数名"和"参数值"！');
        return;
      }
      const params = this.paramList.reduce((obj, param) => {
        obj[param.key.trim()] = param.value.trim();
        return obj;
      }, {});
      this.$emit('set-params', params);
      this.paramList = [{ key: '', value: '' }];
    }
  },
  emits: ['set-params']
};
</script>

<style scoped>
.param-setting-module {
  margin-bottom: 25px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.param-setting-module h4 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.param-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.param-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.param-row {
  display: flex;
  gap: 10px;
  align-items: center;
}

.param-key {
  flex: 1;
  padding: 8px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  color: #2c3e50;
}

.param-value {
  flex: 1;
  padding: 8px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  color: #2c3e50;
}

.param-actions {
  display: flex;
  gap: 10px;
  align-items: center;
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
.btn-send { background: #3498db; color: white; }
.btn-remove { 
  background: #e74c3c; 
  color: white; 
  width: 32px; 
  height: 32px; 
  padding: 0; 
  min-width: auto; 
  border-radius: 50%; 
}

@media (max-width: 768px) {
  .param-row {
    flex-direction: column;
    align-items: stretch;
  }
  
  .param-actions {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>