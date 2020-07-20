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
        const {findAll, clearAlert} = this.props

        clearAlert()

        findAll(userId)
    }

    approveOrder = orderCode => {
        const {userId} = this.props
        const {approve, successAlert, warningAlert} = this.props

        if (window.confirm(`Confrma aprovação do pedido ${orderCode}? Ação não pode ser desfeita.`)) {
            approve({userId, orderCode})
                .then(approvedOrder => successAlert(`Pedido ${approvedOrder.code} aprovado com sucesso.`))
                .catch(({response}) => warningAlert(response.data))
        }
    }

    rejectOrder = orderCode => {
        const {userId} = this.props
        const {reject, successAlert, warningAlert} = this.props

        if (window.confirm(`Confrma rejeição do pedido ${orderCode}? Ação não pode ser desfeita.`)) {
            reject({userId, orderCode})
                .then(rejectedOrder => successAlert(`Pedido ${rejectedOrder.code} rejeitado com sucesso.`))
                .catch(({response}) => warningAlert(response.data))
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
        const {orderModel, alertModel} = dispatchers

        return {
            findAll: orderModel.findAll,
            approve: orderModel.approve,
            reject: orderModel.reject,
            clearAlert: alertModel.clear,
            successAlert: alertModel.success,
            warningAlert: alertModel.warning
        }
    }
)(OrdersTable)