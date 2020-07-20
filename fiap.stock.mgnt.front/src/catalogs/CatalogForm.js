import React, { Component } from 'react'
import {connect} from 'react-redux'

export class CatalogForm extends Component {

    constructor(props) {
        super(props)

        this.state = {
            description: ''
        }
    }
    
    componentDidMount() {
        const {clearAlert} = this.props
        clearAlert()
    }

    catalogFormSubmitHandler = event => {
        event.preventDefault()

        const {userId} = this.props
        const {saveNewCatalogItem, successAlert, warningAlert} = this.props
        const {description} = this.state

        saveNewCatalogItem({userId, description})
            .then(catalogItem => {
                successAlert(`Item ${catalogItem.description} adicionado com sucesso.`)

                this.setState(
                    {
                        ...this.state,
                        description: ''
                    }
                )
            })
            .catch(({response}) => warningAlert(response.data))
    }

    descriptionChangeHandler = event => {
        const description = event.target.value;
        
        this.setState(
            {
                ...this.state,
                description: description
            }
        )
    }

    render() {
        const {description} = this.state

        return (
            <div>
                <div className="align-items-center p-3 my-3 text-white-50 bg-purple rounded shadow-sm">
                    <div className="lh-100 m-4">
                        <h1 className="mb-5 text-white lh-100">Catálogo</h1>

                        <form className="text-white" onSubmit={this.catalogFormSubmitHandler}>
                            <div className="form-group row">
                                <label className="col-sm-2 col-form-label">Descrição:</label>
                                <div className="col-sm-10">
                                    <input type="text" className="form-control" id="colFormLabel" placeholder="Fornecer descrição do item do catálogo..." value={description} onChange={this.descriptionChangeHandler}/>
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
        const {loginModel} = state

        return {
            userId: loginModel.userId
        }
    },
    dispatchers => {
        const {catalogModel, alertModel} = dispatchers

        return {
            saveNewCatalogItem: catalogModel.saveNewCatalogItem,
            clearAlert: alertModel.clear,
            successAlert: alertModel.success,
            warningAlert: alertModel.warning
        }
    }
)(CatalogForm)