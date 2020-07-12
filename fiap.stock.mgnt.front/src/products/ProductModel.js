import { fetchAllProducts, postNewProduct } from './ProductDomain'

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
        },
        async saveNewProduct({userId, product}) {
            return postNewProduct(userId, product)
                .then(productCreated => {
                    this.findAll()

                    return productCreated
                })
        }
    }
}

export default productModel