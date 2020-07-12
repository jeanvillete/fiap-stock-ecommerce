import { fetchAllOrders, postNewOrder } from './OrderDomain'

const defaultState = {
    orders: [],
    cart: {
        products: []
    }
}

const orderModel = {
    state: defaultState,
    reducers: {
        setOrders(currentState, orders) {
            return {...currentState, orders}
        },
        setCart(currentState, cart) {
            return {...currentState, cart}
        },
        addProductToCart(currentState, product) {
            const {cart} = currentState

            const alreadyExist = cart.products.filter(productItem => productItem.code === product.code);
            if (!alreadyExist || !alreadyExist.length) {
                cart.products.push(product)
            }

            return {...currentState, cart}
        },
        removeProductFromCart(currentState, productCode) {
            const {cart} = currentState

            const index = cart.products.map(productItem => productItem.code).indexOf(productCode);

            cart.products.splice(index, 1)

            return {...currentState, cart}
        },
        clearCart(currentState) {
            const cart = {
                products: []
            }
            
            return {...currentState, cart}
        }
    },
    effects: {
        async findAll(userId) {
            return fetchAllOrders(userId)
                .then(this.setOrders)
        },
        async saveNewOrder({userId, order}) {
            return postNewOrder(userId, order)
                .then(orderCreated => {
                    this.findAll(userId)

                    return orderCreated
                })
        }
    }
}

export default orderModel