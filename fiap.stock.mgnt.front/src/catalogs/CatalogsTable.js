import React, { Component } from 'react'
import {connect} from 'react-redux'

export class CatalogsTable extends Component {

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
        const {catalogs} = this.props

        return (
            <div className="my-3 p-3 bg-white rounded shadow-sm">
                <h3 className="border-bottom border-gray pb-2 mb-0">Itens do Catálogo</h3>

                {
                    catalogs.map(catalogItem =>
                        <div key={catalogItem.id} className="media text-muted pt-3">
                            <div className="media-body pb-3 mb-0 lh-125 border-bottom border-gray">
                                <div className="d-flex justify-content-between align-items-center w-100">
                                    <strong className="text-gray-dark">
                                        {
                                            catalogItem.description
                                        }
                                    </strong>
                                    
                                    <span className="d-block pt-1">
                                        <button className="btn btn-sm btn-link" type="button" >Remover</button>
                                        <button className="btn btn-sm btn-link" type="button" >Editar</button>
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
        const {loginModel, catalogModel} = state

        return {
            userId: loginModel.userId,
            catalogs: catalogModel.catalogs
        }
    },
    dispatchers => {
        const {catalogModel} = dispatchers

        return {
            findAll: catalogModel.findAll
        }
    }
)(CatalogsTable)