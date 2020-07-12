import React, { Component } from 'react'
import {connect} from 'react-redux'

export class AddressesTable extends Component {

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

    removeAddressByCodeHandler = addressCode => {
        const {userId} = this.props
        const {removeAddressByCode} = this.props

        if (window.confirm("Confirma a exclusão do endereço? Operação não pode ser desfeita.")) {
            removeAddressByCode({userId, addressCode})
        }
    }

    render() {
        const {addresses} = this.props

        return (
            <div className="my-3 p-3 bg-white rounded shadow-sm">
                <h3 className="border-bottom border-gray pb-2 mb-0">Endereços</h3>

                {
                    addresses.map(addressItem =>
                        <div key={addressItem.code} className="media text-muted pt-3">
                            <div className="media-body pb-3 mb-0 lh-125 border-bottom border-gray">
                                <div className="d-flex justify-content-between align-items-center w-100">
                                    <strong className="text-gray-dark">
                                        { addressItem.zip_code }
                                    </strong>
                                    <strong className="text-gray-dark">
                                        { addressItem.complement }
                                    </strong>
                                    <strong className="text-gray-dark">
                                        { addressItem.city }
                                    </strong>
                                    <strong className="text-gray-dark">
                                        { addressItem.state }
                                    </strong>
                                    <strong className="text-gray-dark">
                                        { addressItem.country }
                                    </strong>
                                    
                                    
                                    <span className="d-block pt-1">
                                        <button className="btn btn-sm btn-link" type="button" onClick={() => this.removeAddressByCodeHandler(addressItem.code)} >Remover</button>
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
        const {loginModel, addressModel} = state

        return {
            userId: loginModel.userId,
            addresses: addressModel.addresses
        }
    },
    dispatchers => {
        const {addressModel} = dispatchers

        return {
            findAll: addressModel.findAll,
            removeAddressByCode: addressModel.removeAddressByCode
        }
    }
)(AddressesTable)