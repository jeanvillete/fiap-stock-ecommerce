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

    approveOrder = orderCode => {
        const {userId} = this.props
        const {approve} = this.props

        if (window.confirm(`Confrma aprovação da ordem ${orderCode}? Ação não pode ser desfeita.`)) {
            approve({userId, orderCode})
        }
    }

    rejectOrder = orderCode => {
        const {userId} = this.props
        const {reject} = this.props

        if (window.confirm(`Confrma rejeição da ordem ${orderCode}? Ação não pode ser desfeita.`)) {
            reject({userId, orderCode})
        }
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
                                        <div className={`badge badge-${orderItem.orderStatus === 'APPROVED' ? 'primary' : orderItem.orderStatus === 'REJECTED' ? 'warning' : ''}`}>
                                            { orderItem.orderStatus }
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
                                    
                                    <span className="d-block pt-1">
                                        <button className="btn btn-primary mr-1" type="button" disabled={orderItem.orderStatus !== 'WAITING_FOR_ANSWER'} onClick={() => this.approveOrder(orderItem.code)}>Aprovar</button>
                                        <button className="btn btn-warning" type="button" disabled={orderItem.orderStatus !== 'WAITING_FOR_ANSWER'} onClick={() => this.rejectOrder(orderItem.code)}>Rejeitar</button>
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
        const {loginModel, orderModel} = state

        return {
            userId: loginModel.userId,
            orders: orderModel.orders
        }
    },
    dispatchers => {
        const {orderModel} = dispatchers

        return {
            findAll: orderModel.findAll,
            approve: orderModel.approve,
            reject: orderModel.reject
        }
    }
)(OrdersTable)