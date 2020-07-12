import React, { Component } from 'react'
import { connect } from 'react-redux'
import {NavLink} from 'react-router-dom'
import PropTypes from 'prop-types'

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
                    <NavLink to='/catalogs' className="nav-link" >
                        Catálogo
                    </NavLink>
                    <NavLink to='/products' className="nav-link" >
                        Produtos
                    </NavLink>
                    <NavLink to='/orders' className="nav-link" >
                        Pedidos
                    </NavLink>
                    <NavLink to='/login' className="nav-link" >
                        Login
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

Menu.propTypes = {
    login: PropTypes.string,
    type: PropTypes.string,
    userId: PropTypes.string
}

export default connect(mapStateToProps, mapDispatchToProps)(Menu)