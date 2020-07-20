import React, { Component } from 'react'
import {connect} from 'react-redux'

export class ProductForm extends Component {

    constructor(props) {
        super(props)

        this.state = {
            product: this.emptyProduct(),
        }
    }

    emptyProduct = () => {
        return {
            catalogId: '',
            price: '',
            quantity: ''
        };
    }
    
    componentDidMount() {
        const {userId} = this.props
        const {findAllCatalogs, clearAlert} = this.props
        const {product} = this.state

        clearAlert()

        findAllCatalogs(userId)
            .then(catalogs => {
                if (catalogs.length) {
                    const catalog = catalogs[0]

                    product.catalogId = catalog.id

                    this.setState(
                        {
                            ...this.state,
                            product
                        }
                    )
                }
            })
    }

    productFormSubmitHandler = event => {
        event.preventDefault()

        const {userId} = this.props
        const {saveNewProduct, successAlert, warningAlert} = this.props
        const {product} = this.state

        saveNewProduct({userId, product})
            .then(productCreated => {
                successAlert(`Produto ${productCreated.code} adicionado com sucesso.`)

                this.setState(
                    {
                        ...this.state,
                        product: this.emptyProduct()
                    }
                )
            })
            .catch(({response}) => warningAlert(response.data))
    }

    catalogChangeHandler = event => {
        const {product} = this.state

        const catalogId = event.target.value;

        product.catalogId = catalogId

        this.setState(
            {
                ...this.state,
                product
            }
        )
    }

    priceChangeHandler = event => {
        const {product} = this.state

        const price = event.target.value;

        product.price = price

        this.setState(
            {
                ...this.state,
                product
            }
        )
    }

    quantityChangeHandler = event => {
        const {product} = this.state

        const quantity = event.target.value;

        product.quantity = quantity

        this.setState(
            {
                ...this.state,
                product
            }
        )
    }

    render() {
        const {product} = this.state
        const {catalogs} = this.props

        return (
            <div>
                <div className="align-items-center p-3 my-3 text-white-50 bg-purple rounded shadow-sm">
                    <div className="lh-100 m-4">
                        <h1 className="mb-5 text-white lh-100">Produtos</h1>

                        <form className="text-white" onSubmit={this.productFormSubmitHandler}>
                            <div className="form-group row">
                                <label className="col-sm-2 col-form-label">Catálogo:</label>
                                <div className="col-sm-10">
                                    <select className="custom-select custom-select mb-3" onChange={this.catalogChangeHandler} value={product.catalogId}>
                                        {
                                            catalogs.map(catalogItem => <option key={catalogItem.id} value={catalogItem.id}>{catalogItem.description}</option>)
                                        }
                                    </select>
                                </div>
                                
                                <label className="col-sm-2 col-form-label">Preço:</label>
                                <div className="col-sm-4">
                                    <input type="text" className="form-control" id="colFormPrice" placeholder="Fornecer preço, apenas números..." onChange={this.priceChangeHandler} value={product.price} />
                                </div>
                                
                                <label className="col-sm-2 col-form-label">Quantidade:</label>
                                <div className="col-sm-4">
                                    <input type="text" className="form-control" id="colFormQuantity" placeholder="Fornecer quantidade, apenas números..." onChange={this.quantityChangeHandler} value={product.quantity} />
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
            </div>
        )
    }
}

export default connect(
    state => {
        const {loginModel, catalogModel} = state

        return {
            userId: loginModel.userId,
            catalogs: catalogModel.catalogs
        }
    },
    dispatchers => {
        const {productModel, catalogModel, alertModel} = dispatchers

        return {
            saveNewProduct: productModel.saveNewProduct,
            findAllCatalogs: catalogModel.findAll,
            clearAlert: alertModel.clear,
            successAlert: alertModel.success,
            warningAlert: alertModel.warning
        }
    }
)(ProductForm)