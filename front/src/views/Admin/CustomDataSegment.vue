<template>
  <div class="control-section component-gap">
    <h3>📊 自定义数据段管理</h3>
    
    <!-- 自定义数据添加 -->
    <div class="data-add-module">
      <h4>添加自定义数据</h4>
      <div class="data-add-form">
        <div class="form-fields">
          <div class="form-field">
            <label>数据名称:</label>
            <input 
              type="text" 
              v-model="newData.name" 
              class="data-input name-input"
              placeholder="如：sensor_temp"
              :disabled="!isConnected"
            >
          </div>
          <div class="form-field">
            <label>数据类型:</label>
            <select 
              v-model="newData.type" 
              class="data-select type-select"
              :disabled="!isConnected"
            >
              <option value="1">整数 (INTEGER)</option>
              <option value="2">布尔 (BOOLEAN)</option>
              <option value="3">浮点 (FLOAT)</option>
              <option value="4">双精度 (DOUBLE)</option>
              <option value="5">字符串 (STRING)</option>
            </select>
          </div>
          <div class="form-field">
            <label>数据值:</label>
            <input 
              :type="getInputType(newData.type)" 
              v-model="newData.value" 
              class="data-input value-input"
              :placeholder="getPlaceholderText(newData.type)"
              :disabled="!isConnected"
            >
          </div>
        </div>
        <button 
          @click="handleAddData" 
          class="btn btn-add"
          :disabled="!isConnected || !isNewDataValid()"
        >
          ➕ 添加到列表
        </button>
      </div>
    </div>

    <!-- 自定义数据列表 -->
    <div class="data-list-module">
      <h4>当前数据列表（{{ customData.length }} 条）</h4>
      <div class="data-table" v-if="customData.length > 0">
        <div class="table-header">
          <div class="col-name">数据名称</div>
          <div class="col-type">数据类型</div>
          <div class="col-value">数据值</div>
          <div class="col-action">操作</div>
        </div>
        <div 
          v-for="(data, index) in customData" 
          :key="index" 
          class="table-row"
        >
          <div class="col-name">{{ data.name }}</div>
          <div class="col-type">{{ getTypeName(data.type) }}</div>
          <div class="col-value">{{ data.value }}</div>
          <div class="col-action">
            <button 
              @click="$emit('remove-custom-data', index)" 
              class="btn btn-remove"
              :disabled="!isConnected"
            >
              删除
            </button>
          </div>
        </div>
      </div>
      <div class="empty-state" v-else>
        暂无数据，请添加自定义数据...
      </div>
    </div>

    <!-- 数据发送控制 -->
    <div class="data-send-module">
      <button 
        @click="$emit('send-custom-data')" 
        class="btn btn-send"
        :disabled="!isConnected || customData.length === 0"
      >
        📤 发送自定义数据段
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CustomDataSegment',
  props: {
    isConnected: {
      type: Boolean,
      required: true,
      default: false
    },
    customData: {
      type: Array,
      required: true,
      default: () => []
    }
  },
  data() {
    return {
      newData: {
        name: '',
        type: '1',
        value: ''
      }
    };
  },
  methods: {
    getInputType(type) {
      switch (type) {
        case '1': return 'number';
        case '2': return 'checkbox';
        case '3': return 'number';
        case '4': return 'number';
        case '5': return 'text';
        default: return 'text';
      }
    },
    getPlaceholderText(type) {
      const map = {
        '1': '请输入整数（如：25）',
        '2': '勾选为true，否则为false',
        '3': '请输入浮点数（如：3.14）',
        '4': '请输入双精度数（如：9.81）',
        '5': '请输入字符串（如："normal"）'
      };
      return map[type] || '请输入数据值';
    },
    getTypeName(type) {
      const map = {
        '1': '整数 (INTEGER)',
        '2': '布尔 (BOOLEAN)',
        '3': '浮点 (FLOAT)',
        '4': '双精度 (DOUBLE)',
        '5': '字符串 (STRING)'
      };
      return map[type] || '未知类型';
    },
    isNewDataValid() {
      if (!this.newData.name.trim()) return false;
      if (this.newData.type === '2') return true;
      return this.newData.value !== '' && this.newData.value !== null && this.newData.value !== undefined;
    },
    handleAddData() {
      if (!this.isNewDataValid()) return;
      
      let dataValue = this.newData.value;
      if (this.newData.type === '2') {
        dataValue = dataValue ? 'true' : 'false';
      }
      
      const dataToAdd = {
        name: this.newData.name.trim(),
        type: this.newData.type,
        value: dataValue
      };
      
      const nameExists = this.customData.some(item => item.name === dataToAdd.name);
      if (nameExists) {
        alert(`数据名称"${dataToAdd.name}"已存在，请更换名称！`);
        return;
      }
      
      this.$emit('add-custom-data', dataToAdd);
      
      this.newData = {
        name: '',
        type: '1',
        value: ''
      };
    }
  },
  emits: ['add-custom-data', 'remove-custom-data', 'send-custom-data']
};
</script>

<style scoped>
.data-add-module {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.data-add-module h4 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.data-add-form {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: center;
}

.form-fields {
  display: flex;
  gap: 15px;
  flex: 1;
  min-width: 600px;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 5px;
  flex: 1;
}

.form-field label {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.data-input, .data-select {
  padding: 8px 12px;
  border: 2px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  color: #2c3e50;
}

.data-list-module {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.data-list-module h4 {
  margin: 0 0 12px 0;
  color: #2c3e50;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.data-table {
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.table-header {
  display: flex;
  background: #2c3e50;
  color: white;
  font-weight: bold;
}

.table-row {
  display: flex;
  background: white;
  border-bottom: 1px solid #e0e0e0;
  transition: background 0.3s;
}

.table-row:hover {
  background: #f1f2f6;
}

.col-name { flex: 2; padding: 12px; }
.col-type { flex: 2; padding: 12px; }
.col-value { flex: 3; padding: 12px; }
.col-action { flex: 1; padding: 12px; text-align: center; }

.empty-state {
  padding: 20px;
  text-align: center;
  color: #7f8c8d;
  font-size: 14px;
  background: white;
  border-radius: 6px;
  border: 1px dashed #e0e0e0;
}

.data-send-module {
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  gap: 10px;
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
.btn-send { background: #3498db; color: white; align-self: flex-start; }
.btn-remove { background: #e74c3c; color: white; padding: 4px 8px; font-size: 12px; }

@media (max-width: 992px) {
  .form-fields {
    min-width: 100%;
    flex-direction: column;
  }
  
  .data-add-form {
    flex-direction: column;
    align-items: stretch;
  }
}

@media (max-width: 768px) {
  .table-header, .table-row {
    flex-direction: column;
  }
  
  .col-name, .col-type, .col-value, .col-action {
    padding: 8px;
    border-bottom: 1px solid #e0e0e0;
  }
  
  .col-action {
    text-align: left;
  }
  
  .btn-send {
    align-self: stretch;
  }
}
</style>