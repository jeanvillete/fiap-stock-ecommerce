import React, { Component } from 'react'
import { connect } from 'react-redux'
import {NavLink} from 'react-router-dom'

export class Menu extends Component {

    constructor(props) {
        super(props)

        this.state = {
        }
    }

    componentDidMount() {
    }

    render() {
        const {userId} = this.props

        if (!userId) return null;

        return (
            <div className="nav-scroller bg-white shadow-sm">
                <nav className="nav nav-underline">
                    <NavLink to='/products' className="nav-link" >
                        Produtos
                    </NavLink>
                    <NavLink to='/orders' className="nav-link" >
                        Pedidos
                    </NavLink>
                    <NavLink to='/addresses' className="nav-link" >
                        Endereços
                    </NavLink>
                </nav>
            </div>
        )
    }
}

const mapStateToProps = state => {
    const {loginModel} = state

    return {
        login: loginModel.login,
        type: loginModel.type,
        userId: loginModel.userId
    }
}

const mapDispatchToProps = dispatchers => {
    return {
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Menu)