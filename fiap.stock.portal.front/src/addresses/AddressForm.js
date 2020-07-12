import React, { Component } from 'react'
import {connect} from 'react-redux'
import Alert from '../alert/Alert'

export class AddressForm extends Component {

    constructor(props) {
        super(props)

        this.state = {
            address: this.emptyAddress(),
            alert: {
                type: null,
                message: null
            }
        }
    }
    
    emptyAddress = () => {
        return {
            zip_code: '',
            complement: '',
            city: '',
            state: '',
            country: ''
        }
    }

    componentDidMount() {
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

    addressFormSubmitHandler = event => {
        event.preventDefault()

        const {userId} = this.props
        const {persistNewAddress} = this.props
        const {address} = this.state

        persistNewAddress({userId, address})
            .then(addressCreated => {
                this.alert('success', `Endereço ${addressCreated.zip_code} adicionado com sucesso.`)

                this.setState(
                    {
                        ...this.state,
                        address: this.emptyAddress()
                    }
                )
            })
            .catch(({response}) => this.alert('warning', response.data))
    }

    zipCodeChangeHandler = event => {
        const zipCode = event.target.value

        const {address} = this.state

        address.zip_code = zipCode

        this.setState({
            ...this.state,
            address
        })
    }

    complementChangeHandler = event => {
        const complement = event.target.value

        const {address} = this.state

        address.complement = complement

        this.setState({
            ...this.state,
            address
        })
    }

    cityChangeHandler = event => {
        const city = event.target.value

        const {address} = this.state

        address.city = city

        this.setState({
            ...this.state,
            address
        })
    }

    stateChangeHandler = event => {
        const state = event.target.value

        const {address} = this.state

        address.state = state

        this.setState({
            ...this.state,
            address
        })
    }

    countryChangeHandler = event => {
        const country = event.target.value

        const {address} = this.state

        address.country = country

        this.setState({
            ...this.state,
            address
        })
    }

    render() {
        const {alert, address} = this.state

        return (
            <div>
                <div className="align-items-center p-3 my-3 text-white-50 bg-purple rounded shadow-sm">
                    <div className="lh-100 m-4">
                        <h1 className="mb-5 text-white lh-100">Endereços</h1>

                        <form className="text-white" onSubmit={this.addressFormSubmitHandler}>
                            <div className="form-group row">
                                <label className="col-sm-2 col-form-label">CEP:</label>
                                <div className="col-sm-4">
                                    <input type="text" className="form-control" id="colFormZipCode" placeholder="Formato esperado 12345-678..." onChange={this.zipCodeChangeHandler} value={address.zip_code} />
                                </div>
                                
                                <label className="col-sm-2 col-form-label">Complemento:</label>
                                <div className="col-sm-4">
                                    <input type="text" className="form-control" id="colFormComplement" placeholder="Rua, Bairro, Número..." onChange={this.complementChangeHandler} value={address.complement} />
                                </div>
                            </div>
                            
                            <div className="form-group row">
                                <label className="col-sm-2 col-form-label">Cidade:</label>
                                <div className="col-sm-4">
                                    <input type="text" className="form-control" id="colFormCity" placeholder="Fornecer cidade..." onChange={this.cityChangeHandler} value={address.city} />
                                </div>
                                
                                <label className="col-sm-2 col-form-label">Estado:</label>
                                <div className="col-sm-4">
                                    <input type="text" className="form-control" id="colFormState" placeholder="Fornecer estado..." onChange={this.stateChangeHandler} value={address.state} />
                                </div>
                            </div>
                            
                            <div className="form-group row">
                                <label className="col-sm-2 col-form-label">País:</label>
                                <div className="col-sm-4">
                                    <input type="text" className="form-control" id="colFormCountry" placeholder="Fornecer país..." onChange={this.countryChangeHandler} value={address.country} />
                                </div>
                            </div>

                            <div className="form-group row">
                                <div className="col-sm-2" />
                                <div className="col-sm-2">
                                    <button className="btn btn-lg btn-primary btn-block" type="submit">Adicionar</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <Alert type={alert.type} message={alert.message}/>
            </div>
        )
    }
}

export default connect(
    state => {
        const {loginModel} = state

        return {
            userId: loginModel.userId
        }
    },
    dispatchers => {
        const {addressModel} = dispatchers

        return {
            persistNewAddress: addressModel.persistNewAddress
        }
    }
)(AddressForm)