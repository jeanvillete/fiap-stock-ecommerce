import React, { Component } from 'react'
import {connect} from 'react-redux'

export class OrdersTable extends Component {

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
        const {orders} = this.props

        return (
            <div className="my-3 p-3 bg-white rounded shadow-sm">
                <h3 className="border-bottom border-gray pb-2 mb-0">Pedidos</h3>

                {
                    orders.map(orderItem =>
                        <div key={orderItem.code} className="media text-muted pt-3">
                            <div className="media-body pb-3 mb-0 lh-125 border-bottom border-gray">
                                <div className="d-flex justify-content-between align-items-center w-100">
                                    <strong className="text-gray-dark">
                                        <div>
                                            { orderItem.code }
                                        </div>
                                        <div className={`badge badge-${orderItem.status === 'APPROVED' ? 'primary' : orderItem.status === 'REJECTED' ? 'warning' : ''}`}>
                                            { orderItem.status }
                                        </div>
                                    </strong>
                                    <strong className="text-gray-dark">
                                        {
                                            orderItem.products
                                                .map(productItem =>
                                                    <div key={productItem.code}>
                                                        <small className='ml-2'>product code:</small> { productItem.code }
                                                        <small className='ml-2'>quantity:</small> { productItem.quantity }
                                                    </div>
                                                )
                                        }
                                    </strong>
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
        const {loginModel, orderModel} = state

        return {
            userId: loginModel.userId,
            orders: orderModel.orders
        }
    },
    dispatchers => {
        const {orderModel} = dispatchers

        return {
            findAll: orderModel.findAll
        }
    }
)(OrdersTable)