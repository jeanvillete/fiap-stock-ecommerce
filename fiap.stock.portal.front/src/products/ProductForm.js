import React, { Component } from 'react'
import {connect} from 'react-redux'

export class ProductForm extends Component {

    constructor(props) {
        super(props)

        this.state = {
            filter: ''
        }
    }
    
    componentDidMount() {
    }


    productSearchFormSubmitHandler = event => {
        event.preventDefault()


    }

    filterChangeHandler = event => {
        const filter = event.target.value

        this.setState({
            ...this.state,
            filter: filter
        })
    }

    render() {
        return (
            <div>
                <div className="align-items-center p-3 my-3 text-white-50 bg-purple rounded shadow-sm">
                    <div className="lh-100 m-4">
                        <h1 className="mb-5 text-white lh-100">Produtos</h1>

                        <form className="text-white" onSubmit={this.productSearchFormSubmitHandler}>
                            <div className="form-group row">
                                <label className="col-sm-2 col-form-label">Filtro:</label>
                                <div className="col-sm-10">
                                    <input type="text" className="form-control" id="colFormFilter" placeholder="Fornecer filtro com código ou descrição..." onChange={this.filterChangeHandler} />
                                </div>
                            </div>

                            <div className="form-group row">
                                <div className="col-sm-2" />
                                <div className="col-sm-2">
                                    <button className="btn btn-lg btn-primary btn-block" type="submit">Buscar</button>
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
        const {loginModel} = state

        return {
            userId: loginModel.userId
        }
    },
    dispatchers => {
        const {productModel} = dispatchers

        return {
        }
    }
)(ProductForm)