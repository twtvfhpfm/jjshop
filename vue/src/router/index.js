import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import My from '@/components/My'
import Locations from '@/components/Locations'
import LocationEdit from '@/components/LocationEdit'
import Cart from '@/components/Cart'
import SubmitOrder from '@/components/SubmitOrder'
import Gallery from '@/components/Gallery'
import GoodsList from '@/components/GoodsList'
import GoodsInfo from '@/components/GoodsInfo'
import Register from '@/components/Register'
import Manager from '@/components/Manager'
import Pay from '@/components/Pay'
import CategoryManager from '@/components/CategoryManager'
import GoodsManager from '@/components/GoodsManager'
import GoodsInfoManager from '@/components/GoodsInfoManager'
import SkuManager from '@/components/SkuManager'
import SkuListManager from '@/components/SkuListManager'
import GoodsOrderList from '@/components/GoodsOrderList'
import GoodsOrderInfo from '@/components/GoodsOrderInfo'
import GoodsOrderManager from '@/components/GoodsOrderManager'
import GoodsOrderInfoManager from '@/components/GoodsOrderInfoManager'
import ChargeCodeManager from '@/components/ChargeCodeManager'
import Alipay from '@/components/Alipay'
import UserManager from '@/components/UserManager'
import BillManager from '@/components/BillManager'
import BatchSkuListManager from '@/components/BatchSkuListManager'
import PriceReduceManager from '@/components/PriceReduceManager'
import CouponOffer from '@/components/CouponOffer'
import Coupon from '@/components/Coupon'
import CouponManager from '@/components/CouponManager'
import PrizeManager from '@/components/PrizeManager'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: '主页',
      component: Home,
      hidden: true,
      children: [
        {
          path: '/coupon',
          name: 'Coupon',
          component: Coupon,
          hidden: true,
          meta:{
            requireAuth:true
          }
        }, 
        {
          path: '/couponmanager',
          name: 'CouponManager',
          component: CouponManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        }, 
        {
          path: '/prizemanager',
          name: 'PrizeManager',
          component: PrizeManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        }, 
        {
          path: '/couponoffer',
          name: 'CouponOffer',
          component: CouponOffer,
          hidden: true,
          meta:{
            requireAuth:true
          }
        }, 
        {
          path: '/pricereducemanager',
          name: 'PriceReduceManager',
          component: PriceReduceManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        }, 
        {
          path: '/login',
          name: 'Login',
          component: Login,
          hidden: true
        }, 
        {
          path: '/register',
          name: 'Register',
          component: Register,
          hidden: true
        }, 
        {
          path: '/cart',
          name: '购物车',
          component: Cart,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/gallery',
          name: '仓库',
          component: Gallery,
          hidden: true,
        },
        {
          path: '/goodslist',
          name: '商品列表',
          component: GoodsList,
          hidden: true,
        },
        {
          path: '/goodsinfo',
          name: '商品信息',
          component: GoodsInfo,
          hidden: true
        },
        {
          path: '/my',
          name: '我的',
          component: My,
          hidden: true
        },
        {
          path: '/goodsorderlist',
          name: '订单列表',
          component: GoodsOrderList,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/goodsorderinfo',
          name: '订单详情',
          component: GoodsOrderInfo,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/manager',
          name: '管理',
          component: Manager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/categorymanager',
          name: '分类管理',
          component: CategoryManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/goodsmanager',
          name: '商品管理',
          component: GoodsManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/goodsinfomanager',
          name: '商品详情管理',
          component: GoodsInfoManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/skumanager',
          name: '商品规格管理',
          component: SkuManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/skulistmanager',
          name: '商品库存管理',
          component: SkuListManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/batchskulistmanager',
          name: '商品库存批量管理',
          component: BatchSkuListManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/goodsordermanager',
          name: '订单管理',
          component: GoodsOrderManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/goodsorderinfomanager',
          name: '订单详情管理',
          component: GoodsOrderInfoManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/chargecodemanager',
          name: '收钱码管理',
          component: ChargeCodeManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/billmanager',
          name: '账单管理',
          component: BillManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/usermanager',
          name: '会员管理',
          component: UserManager,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/locations',
          name: '收货地址',
          component: Locations,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/location_edit',
          name: '编辑收货地址',
          component: LocationEdit,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/submitorder',
          name: '提交订单',
          component: SubmitOrder,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/pay',
          name: '支付订单',
          component: Pay,
          hidden: true,
          meta:{
            requireAuth:true
          }
        },
        {
          path: '/alipay',
          name: '支付宝支付',
          component: Alipay,
          hidden: true
        }
      ]
    }
  ]
})
