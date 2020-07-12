import React, { Component } from 'react'
import {connect} from 'react-redux'
import Alert from '../alert/Alert'

export class CatalogForm extends Component {

    constructor(props) {
        super(props)

        this.state = {
            description: null,
            alert: {
                type: null,
                message: null
            }
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

    catalogFormSubmitHandler = event => {
        event.preventDefault()

        const {userId} = this.props
        const {saveNewCatalogItem} = this.props
        const {description} = this.state

        saveNewCatalogItem({userId, description})
            .then(userId => this.alert('success', `Item ${description} adicionado com sucesso.`))
            .catch(({response}) => this.alert('warning', response.data))

        this.setState(
            {
                ...this.state,
                description: ''
            }
        )
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
        const {alert, description} = this.state

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
        const {catalogModel} = dispatchers

        return {
            saveNewCatalogItem: catalogModel.saveNewCatalogItem
        }
    }
)(CatalogForm)