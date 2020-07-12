import React, { Component } from 'react'
import {connect} from 'react-redux'

export class ProductsTable extends Component {

    constructor(props) {
        super(props)

        this.state = {
        }
    }
    
    componentDidMount() {
        const {userId} = this.props
        const {findAll} = this.props

        findAll(userId)
    }

    render() {
        const {products} = this.props

        return (
            <div className="my-3 p-3 bg-white rounded shadow-sm">
                <h3 className="border-bottom border-gray pb-2 mb-0">Produtos</h3>

                {
                    products.map(productItem =>
                        <div key={productItem.code} className="media text-muted pt-3">
                            <div className="media-body pb-3 mb-0 lh-125 border-bottom border-gray">
                                <div className="d-flex justify-content-between align-items-center w-100">
                                    <strong className="text-gray-dark">
                                        { productItem.code }
                                    </strong>
                                    <strong className="text-gray-dark">
                                        { productItem.description }
                                    </strong>
                                    <strong className="text-gray-dark">
                                        { productItem.price }
                                    </strong>
                                    <strong className="text-gray-dark">
                                        { productItem.quantity }
                                    </strong>
                                    
                                    <span className="d-block pt-1">
                                        <button className="btn btn-sm btn-link" type="button" >Remover</button>
                                        <button className="btn btn-sm btn-link" type="button" >Editar</button>
                                    </span>

                                </div>
                            </div>
                        </div>
                    )
                }
            </div>
        )
    }
}

export default connect(
    state => {
        const {loginModel, productModel} = state

        return {
            userId: loginModel.userId,
            products: productModel.products
        }
    },
    dispatchers => {
        const {productModel} = dispatchers

        return {
            findAll: productModel.findAll
        }
    }
)(ProductsTable)