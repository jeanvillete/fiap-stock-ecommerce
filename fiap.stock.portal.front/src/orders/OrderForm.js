import React, { Component } from 'react'
import {connect} from 'react-redux'
import Alert from '../alert/Alert'

export class OrderForm extends Component {

    constructor(props) {
        super(props)

        this.state = {
            alert: {
                type: null,
                message: null
            },
            products: [],
            address: {
                code: ''
            }
        }
    }
    
    componentDidMount() {
        const {userId} = this.props
        const {findAllAddresses} = this.props

        this.updateProductsOnState()

        findAllAddresses(userId)
            .then(addresses => {
                if (addresses.length) {
                    const address = addresses[0]

                    this.setState(
                        {
                            ...this.state,
                            address
                        }
                    )
                }
            })
    }

    updateProductsOnState = () => {
        const {cart} = this.props

        this.setState(
            {
                ...this.state,
                products: cart.products
            }
        )
    }

    alert = (type, message) => {
        type = type || null
        message = message || null

        this.setState(
            {
                ...this.state,
                alert: {
                    type: type,
                    message: message
                }
            }
        )
    }

    quantityChangeHandler = event => {
        const quantity = event.target.value

        // TODO
    }

    removeProductFromCart = productCode => {
        const {removeProductFromCart} = this.props

        removeProductFromCart(productCode)

        this.updateProductsOnState()
    }

    addressChangeHandler = event => {
        const addressCode = event.target.value

        const {addresses} = this.props
        
        const index = addresses.map(addressItem => addressItem.code).indexOf(addressCode)
        const address = addresses[index]

        this.setState(
            {
                ...this.state,
                address
            }
        )
    }

    orderFormSubmitHandler = event => {
        event.preventDefault()

        const {userId} = this.props
        const {saveNewOrder, clearCart} = this.props
        const {address, products} = this.state

        const order = {address, products}

        saveNewOrder({userId, order})
            .then(orderCreated => {
                this.alert('success', `Pedido criado e com código ${orderCreated.code} gerado.`)

                clearCart()
                this.updateProductsOnState()
            })
            .catch(({response}) => this.alert('warning', response.data))
    }

    render() {
        const {alert, products, address} = this.state
        const {addresses} = this.props
        
        return (
            <div>
                <div className="align-items-center p-3 my-3 text-white-50 bg-purple rounded shadow-sm">
                    <div className="lh-100 m-4">
                        <h1 className="mb-5 text-white lh-100">Pedidos</h1>

                        {
                            products.length
                            ?
                            <form className="text-white" onSubmit={this.orderFormSubmitHandler}>
                                <div className="form-group row">
                                    <label className="col-sm-2 col-form-label">Endereço:</label>
                                    <div className="col-sm-10">
                                        <select className="custom-select custom-select mb-3" onChange={this.addressChangeHandler} value={address.code}>
                                            {
                                                addresses.map(addressItem => 
                                                    <option key={addressItem.code} value={addressItem.code}>
                                                        {addressItem.zip_code}, {addressItem.complement}, {addressItem.city}, {addressItem.state}, {addressItem.country}
                                                    </option>
                                                )
                                            }
                                        </select>
                                    </div>
                                </div>

                                {
                                    products.map(productItem => 
                                        <div key={productItem.code} className="form-group row">
                                            <label className="col-sm-2 col-form-label">Código do Produto:</label>
                                            <div className="col-sm-4">
                                                <input type="text" className="form-control" readOnly={true} value={productItem.code} />
                                            </div>
                                            
                                            <label className="col-sm-2 col-form-label">Quantidade:</label>
                                            <div className="col-sm-1">
                                                <input type="text" className="form-control" value={productItem.quantity} onChange={this.quantityChangeHandler} />
                                            </div>

                                            <div className="col-sm-3">
                                                <button className="btn btn-warning" type="button" onClick={() => this.removeProductFromCart(productItem.code)}>remover</button>
                                            </div>
                                        </div>
                                    )
                                }

                                <div className="form-group row">
                                    <div className="col-sm-2" />
                                    <div className="col-sm-2">
                                        <button className="btn btn-lg btn-primary btn-block" type="submit">Adicionar</button>
                                    </div>
                                </div>
                            </form>
                            :
                            null
                        }
                    </div>
                </div>

                <Alert type={alert.type} message={alert.message}/>
            </div>
        )
    }
}

export default connect(
    state => {
        const {loginModel, orderModel, addressModel} = state

        return {
            userId: loginModel.userId,
            cart: orderModel.cart,
            addresses: addressModel.addresses
        }
    },
    dispatchers => {
        const {orderModel, addressModel} = dispatchers

        return {
            removeProductFromCart: orderModel.removeProductFromCart,
            clearCart: orderModel.clearCart,
            saveNewOrder: orderModel.saveNewOrder,
            findAllAddresses: addressModel.findAll
        }
    }
)(OrderForm)