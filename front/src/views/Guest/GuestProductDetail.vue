<template>
    <div class="product-detail-page section-padding">
        <!-- 两侧动图 需要添加实际动图 -->
        <!-- <div class="decor-gif decor-gif-left"></div>
        <div class="decor-gif decor-gif-right"></div> -->
        <div class="container mx-auto px-4">
            <!-- 面包屑导航 -->
            <div class="breadcrumbs mb-6">
                <router-link to="/">首页</router-link>
                <span class="mx-2">/</span>
                <router-link :to="{ name: 'GuestProductList' }">成果展示</router-link>
                <span class="mx-2">/</span>
                <span>{{ product.name }}</span>
            </div>

            <!-- 轮播图容器 -->
            <div class="product-carousel relative max-w-5xl mx-auto h-96 mb-8">
                <!-- 左侧导航按钮，只有当前不是第一张时显示 -->
                <button class="carousel-btn left-btn" @click="prevSlide" v-if="currentIndex > 0">←</button>
                <!-- 右侧导航按钮，只有当前不是最后一张时显示 -->
                <button class="carousel-btn right-btn" @click="nextSlide"
                    v-if="currentIndex < productImageList.length - 1">→</button>

                <!--轮播图片容器-->
                <div class="carousel-images">
                    <img v-for="(img, idx) in productImageList" :key="idx" :src="img" :alt="`产品图${idx + 1}`"
                        class="carousel-img cursor-pointer" @click="showZoomedImg(idx)" title="点击图片查看"
                        :style="getImageStyle(idx)" loading="lazy" @error="handleImageError">
                </div>
            </div>

            <!-- 产品信息区域 -->
            <div class="product-info max-w-3xl mx-auto text-gray-700 leading-relaxed">
                <h3 class="text-xl font-bold mb-4 text-gray-800">{{ product.name }}</h3>
                <p class="mb-4">{{ product.desc }}</p>
                <h4 class="text-lg font-bold mb-2 text-gray-800">产品详情</h4>
                <p class="mb-4">{{ product.detail }}</p>
            </div>

            <!--图片放大查看层-->
            <div class="zoom-mask" v-if="isZoomed" @click="handleMaskClick">
                <div class="zoom-controls">
                    <button @click="zoomOut" class="control-btn">-</button>
                    <span class="zoom-percent">{{ Math.round(scale * 100) }}%</span>
                    <button @click="zoomIn" class="control-btn">+</button>
                    <button @click="resetZoom" class="reset-btn">重置</button>
                </div>

                <!-- 全屏容器 -->
                <div class="zoom-fullscreen" ref="zoomFullscreen">
                    <img :src="currentZoomImg" alt="放大的产品图片" class="zoom-fullscreen-img" :style="{
                        transform: `scale(${scale}) translate(${position.x}px, ${position.y}px)`,
                        cursor: isDragging ? 'grabbing' : 'grab'
                    }" ref="zoomImg" @load="onImageLoad" @mousedown="startLongPress" @mouseup="endLongPress"
                        @mouseleave="endLongPress" @click.stop>
                </div>

                <!--关闭按钮-->
                <button class="zoom-close" @click="hideZoomedImg">×</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'GuestProductDetail',
    data() {
        return {
            product: {},
            isZoomed: false,
            scale: 1,
            isDragging: false,
            startX: 0,
            startY: 0,
            dragStartPosition: { x: 0, y: 0 },
            position: { x: 0, y: 0 },
            currentZoomImg: '',
            productImageList: [],
            currentIndex: 1,
            resizeObserver: null,
            longPressTimer: null,
            isLongPressing: false,
            hasDragged: false
        }
    },
    created() {
        const productId = parseInt(this.$route.params.id);
        // 仅保留P600科研无人机数据，删除其他所有产品
        const productList = [
            {
                id: 1,
                name: 'Prometheus600科研无人机',
                desc: 'P600系列第四代产品，深度融合Prometheus和SpireCV，具备超远距离目标检测与跟踪能力。',
                detail: 'Prometheus600科研无人机开发平台(简称P600)，是P600系列第四代产品，将两大开源项目Prometheus和SpireCV进行了深度融合，配合40倍变焦光电吊舱，实现超远距离目标检测与跟踪，支持不同高度下的自主降落；光电吊舱可兼作空中目标巡检，满足多场景监测需求；配合二维平面雷达，实现三维空间路径规划(EGO-Planner)，提升飞行自主性与安全性。',
                // 替换为本地图片路径（Vue需用require导入静态资源）
                imageList: [
                    require('@/assets/images/P6001.jpg'),
                    require('@/assets/images/P6002.jpg'),
                    require('@/assets/images/P6003.jpg')
                ]
            }
        ];

        this.product = productList.find(item => item.id === productId) || {};
        // 若传入非1的ID，跳转到首页（仅保留P600一个产品）
        if (!this.product.id) {
            this.$router.push('/');
            return;
        }
        this.productImageList = this.product.imageList || [];
        this.currentIndex = Math.min(1, this.productImageList.length - 1);
    },
    beforeUnmount() {
        // 移除键盘和鼠标事件监听
        document.removeEventListener('keydown', this.handleEscKey);

        if (this.resizeObserver) {
            this.resizeObserver.disconnect();
        }

        // 清理长按计时器
        if (this.longPressTimer) {
            clearTimeout(this.longPressTimer);
        }
    },
    mounted() {
        this.resizeObserver = new ResizeObserver(() => {
            if (this.isZoomed) {
                this.centerImage();
            }
        });
        if (this.$refs.zoomFullscreen) {
            this.resizeObserver.observe(this.$refs.zoomFullscreen);
        }
        document.addEventListener('keydown', this.handleEscKey);
    },
    watch: {
        scale(newVal) {
            if (this.isZoomed && newVal <= 1) {
                this.centerImage();
            }
        }
    },
    methods: {
        /**
         * 处理图片加载错误
         */
        handleImageError(e) {
            console.error('图片加载失败:', e.target.src);
            // 确保默认图路径正确
            e.target.src = require('@/assets/images/default.gif');
            e.target.alt = '图片加载失败';
        },

        /**
         * 图片加载完成后的回调
         */
        onImageLoad() {
            if (this.isZoomed) {
                this.centerImage();
            }
        },

        /**
         * 显示放大图片
         */
        showZoomedImg(idx) {
            this.currentZoomImg = this.productImageList[idx];
            this.isZoomed = true;
            this.scale = 1;
            this.position = { x: 0, y: 0 };
            this.hasDragged = false;
            document.body.style.overflow = 'hidden';
            document.addEventListener('mousemove', this.handleDrag);
            document.addEventListener('wheel', this.handleWheel, { passive: false });
        },

        /**
         * 隐藏放大图片
         */
        hideZoomedImg() {
            this.isZoomed = false;
            document.body.style.overflow = 'auto';
            document.removeEventListener('mousemove', this.handleDrag);
            document.removeEventListener('wheel', this.handleWheel);
        },

        /**
         * 处理遮罩层点击
         */
        handleMaskClick(e) {
            // 只有当点击的是遮罩层本身（不是子元素）且没有进行拖拽时才关闭
            if (e.target === e.currentTarget && !this.hasDragged) {
                this.hideZoomedImg();
            }
        },

        /**
         * 放大图片
         */
        zoomIn() {
            if (this.scale < 3) {
                this.scale += 0.1;
            }
        },

        /**
         * 缩小图片
         */
        zoomOut() {
            if (this.scale > 0.5) {
                this.scale -= 0.1;
            }
        },

        /**
         * 重置缩放
         */
        resetZoom() {
            this.scale = 1;
            this.position = { x: 0, y: 0 };
            this.hasDragged = false;
        },

        /**
         * 开始长按（鼠标）
         */
        startLongPress(e) {
            e.preventDefault();
            e.stopPropagation();

            // 重置拖拽标记
            this.hasDragged = false;

            // 设置长按计时器，300ms后开始拖拽
            this.longPressTimer = setTimeout(() => {
                this.isLongPressing = true;
                this.startDrag(e);
            }, 300);
        },

        /**
         * 结束长按
         */
        endLongPress() {
            // 清除长按计时器
            if (this.longPressTimer) {
                clearTimeout(this.longPressTimer);
                this.longPressTimer = null;
            }

            // 如果正在长按，结束拖拽
            if (this.isLongPressing) {
                this.isLongPressing = false;
                this.endDrag();

                // 延迟一小段时间重置拖拽标记，避免点击事件触发
                setTimeout(() => {
                    this.hasDragged = false;
                }, 100);
            }
        },

        /**
         * 开始拖拽
         */
        startDrag(e) {
            this.isDragging = true;
            this.startX = e.clientX;
            this.startY = e.clientY;
            this.dragStartPosition = { ...this.position };
        },

        /**
         * 处理拖拽
         */
        handleDrag(e) {
            if (!this.isDragging || !this.isLongPressing) return;

            const deltaX = e.clientX - this.startX;
            const deltaY = e.clientY - this.startY;

            // 只有当移动距离超过阈值时才标记为拖拽
            if (Math.abs(deltaX) > 5 || Math.abs(deltaY) > 5) {
                this.hasDragged = true;
            }

            this.position = {
                x: this.dragStartPosition.x + deltaX,
                y: this.dragStartPosition.y + deltaY
            };
        },

        /**
         * 结束拖拽
         */
        endDrag() {
            this.isDragging = false;
        },

        /**
         * 处理ESC键
         */
        handleEscKey(e) {
            if (e.key === 'Escape' && this.isZoomed) this.hideZoomedImg();
        },

        /**
         * 居中图片
         */
        centerImage() {
            this.position = { x: 0, y: 0 };
        },

        /**
         * 获取图片样式
         */
        getImageStyle(idx) {
            if (idx === this.currentIndex) {
                return {
                    width: '40%',
                    height: '100%',
                    zIndex: 30,
                    filter: 'blur(0px)',
                    opacity: 1,
                    left: '30%',
                    top: '0'
                };
            }
            else if (idx === this.currentIndex - 1) {
                return {
                    width: '30%',
                    height: '80%',
                    zIndex: 20,
                    filter: 'blur(3px)',
                    opacity: 0.9,
                    left: '5%',
                    top: '10%'
                };
            }
            else if (idx === this.currentIndex + 1) {
                return {
                    width: '30%',
                    height: '80%',
                    zIndex: 20,
                    filter: 'blur(3px)',
                    opacity: 0.9,
                    left: '65%',
                    top: '10%'
                };
            }
            else {
                return {
                    display: 'none'
                };
            }
        },

        /**
         * 上一张
         */
        prevSlide() {
            if (this.productImageList.length <= 1) return;
            if (this.currentIndex > 0) {
                this.currentIndex--;
            }
        },

        /**
         * 下一张
         */
        nextSlide() {
            if (this.productImageList.length <= 1) return;
            if (this.currentIndex < this.productImageList.length - 1) {
                this.currentIndex++;
            }
        },

        /**
         * 处理鼠标滚轮缩放
         */
        handleWheel(e) {
            if (!this.isZoomed) return;

            e.preventDefault();
            const delta = e.deltaY > 0 ? -0.1 : 0.1;
            const newScale = Math.min(Math.max(this.scale + delta, 0.5), 3);
            this.scale = newScale;
        }
    }
}
</script>

<style scoped>
:root {
    --primary-color: #014a95;
    /* 主色 */
}

/*装饰动图样式*/
/* .decor-gif {
    position: absolute;
    z-index: 1;
    opacity: 0.2;
    pointer-events: none;
    background-repeat: no-repeat;
    background-size: contain;
} */

/* 左侧动图 */
/* .decor-gif-left {
    width: 300px;
    height: 300px;
    top: 150px;
    left: -20px;
    background-image: url(@/assets/images/dongtu1.gif);
} */

/* 右侧动图 */
/* .decor-gif-right {
    width: 320px;
    height: 320px;
    bottom: 20px;
    right: -20px;
    background-image: url(@/assets/images/dongtu2.gif);
} */

/* 详情页父容器定位调整（确保动图相对页面定位） */
.product-detail-page {
    position: relative;
    overflow: hidden;
}

/* 轮播图容器 */
.product-carousel {
    position: relative;
    width: 100%;
    height: 60vh;
    max-height: 450px;
    min-height: 250px;
    overflow: hidden;
}

/*轮播按钮样式 */
.carousel-btn {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    width: 48px;
    height: 48px;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.8);
    color: var(--primary-color);
    border: none;
    font-size: 20px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 50;
    transition: all 0.3s ease;
}

.carousel-btn:hover {
    background: var(--primary-color);
    color: #014a95;
}

.left-btn {
    left: 20px;
}

.right-btn {
    right: 20px;
}

/*轮播图片容器 */
.carousel-images {
    position: relative;
    width: 100%;
    height: 100%;
}

.carousel-img {
    position: absolute;
    object-fit: cover;
    border-radius: 8px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}

/* 悬停时取消模糊 */
.carousel-img:hover {
    transform: scale(1.02);
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
    z-index: 40;
    filter: blur(0px) !important;
}

/* 通用样式 */
.section-padding {
    padding: 2rem 0;
}

.breadcrumbs {
    font-size: 0.9rem;
    color: #666;
    white-space: nowrap;
    /* 禁止换行 */
    overflow: hidden;
    /* 隐藏溢出部分 */
    text-overflow: ellipsis;
    /* 溢出显示省略号 */
}

.breadcrumbs a {
    color: #666;
    text-decoration: none;
}

.breadcrumbs a:hover {
    color: var(--primary-color);
}

.product-info {
    padding: 0 1rem;
}

.max-w-3xl {
    max-width: 70rem;
}

/* 放大层样式 */
.zoom-mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.85);
    z-index: 9999;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px;
    overflow: hidden;
}

.zoom-controls {
    position: absolute;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    gap: 12px;
    align-items: center;
    z-index: 10;
}

.control-btn {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: #333;
    color: white;
    border: none;
    font-size: 18px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background-color 0.3s;
}

.control-btn:hover {
    background: var(--primary-color);
}

.zoom-percent {
    color: white;
    background: rgba(0, 0, 0, 0.5);
    padding: 4px 10px;
    border-radius: 12px;
    font-size: 14px;
}

.reset-btn {
    padding: 4px 12px;
    background: #555;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.reset-btn:hover {
    background: var(--primary-color);
}

/* 全屏容器 */
.zoom-fullscreen {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.zoom-fullscreen-img {
    display: block;
    max-width: 90vw;
    max-height: 80vh;
    width: auto;
    height: auto;
    transition: transform 0.1s ease;
    object-fit: contain;
    cursor: grab;
}

.zoom-close {
    position: absolute;
    top: 20px;
    right: 30px;
    color: #fff;
    font-size: 28px;
    font-weight: bold;
    background: transparent;
    border: none;
    cursor: pointer;
    transition: color 0.3s ease;
    z-index: 10;
}

.zoom-close:hover {
    color: var(--primary-color);
}

/* 响应式调整：平板及以下（768px是常见平板竖屏宽度） */
@media (max-width: 768px) {
    .product-carousel {
        height: 50vh;
        max-height: 300px;
        margin-bottom: 1rem;
        /* 减少底部间距，避免内容过长 */
    }

    /* 轮播按钮：增大触控区域，优化位置 */
    .carousel-btn {
        width: 40px;
        height: 40px;
        font-size: 18px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
        /* 增加阴影，提高辨识度 */
    }

    .left-btn {
        left: 8px;
        /* 更靠近边缘，节省空间 */
    }

    .right-btn {
        right: 8px;
    }

    /* 装饰动图：缩小并调整位置，避免遮挡内容 */
    .decor-gif-left {
        width: 200px;
        height: 200px;
        top: 100px;
        left: -50px;
        transform: scale(0.5);
    }

    .decor-gif-right {
        width: 220px;
        height: 220px;
        bottom: 0;
        right: -60px;
        transform: scale(0.4);
    }

    /* 产品信息区域：调整字体和间距，提高可读性 */
    .product-info {
        padding: 0 0.8rem;
    }

    .product-info h3 {
        font-size: 1.3rem;
        /* 略小字号，适配小屏幕 */
    }

    .product-info p {
        font-size: 0.95rem;
        line-height: 1.6;
        /* 增加行高，方便阅读 */
    }

    /* 放大层适配平板 */
    .zoom-fullscreen-img {
        max-width: 95vw;
        max-height: 80vh;
        /* 降低最大高度，避免超出屏幕 */
    }

    .zoom-controls {
        top: 15px;
        gap: 10px;
        /* 缩小间距，节省空间 */
    }

    .zoom-close {
        top: 15px;
        right: 20px;
        /* 稍微左移，避免超出屏幕 */
        font-size: 24px;
    }
}

/* 小屏手机（480px以下，如iPhone SE等） */
@media (max-width: 480px) {
    .product-carousel {
        height: 40vh;
        max-height: 220px;
        /* 进一步降低高度，适配小屏 */
    }

    /* 轮播图样式：优化小屏显示，避免左右偏移过多 */
    .carousel-img {
        border-radius: 4px;
        /* 减小圆角，节省空间 */
    }

    /* 重写轮播图位置样式，适配小屏布局 */
    .carousel-img:nth-child(1) {
        left: 5% !important;
        /* 强制居中对齐 */
        width: 90% !important;
    }

    /* 轮播按钮：进一步增大触控区域 */
    .carousel-btn {
        width: 44px;
        height: 44px;
    }

    /* 装饰动图：小屏直接隐藏，避免干扰核心内容 */
    .decor-gif {
        display: none;
    }

    /* 面包屑：缩小字号，避免换行混乱 */
    .breadcrumbs {
        font-size: 0.8rem;
    }

    /* 页面标题：适配小屏 */
    .text-center h2 {
        font-size: 1.6rem;
    }

    /* 产品信息：进一步调整字号 */
    .product-info h3 {
        font-size: 1.2rem;
    }

    .product-info p {
        font-size: 0.9rem;
    }

    /* 放大层：优化小屏交互 */
    .zoom-controls {
        gap: 6px;
        top: 10px;
    }

    .control-btn {
        width: 32px;
        height: 32px;
        font-size: 16px;
        /* 保持按钮内文字清晰 */
    }

    .zoom-percent {
        font-size: 11px;
        padding: 2px 6px;
    }

    .reset-btn {
        padding: 2px 6px;
        font-size: 11px;
    }

    .zoom-close {
        right: 15px;
        font-size: 22px;
    }

    .zoom-fullscreen-img {
        max-height: 75vh;
        /* 留出更多空间给控制栏 */
    }
}
</style>