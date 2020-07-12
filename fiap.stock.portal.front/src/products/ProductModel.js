import { fetchAllProducts } from './ProductDomain'

const defaultState = {
    products: []
}

const productModel = {
    state: defaultState,
    reducers: {
        setProducts(currentState, products) {
            return {...currentState, products}
        }
    },
    effects: {
        async findAll(userId) {
            return fetchAllProducts(userId)
                .then(this.setProducts)
        }
    }
}

export default productModel