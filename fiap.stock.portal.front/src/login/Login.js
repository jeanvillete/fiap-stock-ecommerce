import React, { Component } from 'react'
import {connect} from 'react-redux'
import Alert from '../alert/Alert'

import '../signin.css';

export class Login extends Component {

    constructor(props) {
        super(props)

        this.state = {
            alert: {
                type: null,
                message: null
            }
        }
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

    loginFormSubmitHandler = event => {
        event.preventDefault()

        const {doLogin, login} = this.props

        doLogin(login)
            .then(() => this.alert('success', `Usuário ${login} logado com sucesso.`))
            .catch(({response}) => this.alert('warning', response.data))
    }

    loginChangeHandler = event => {
        const login = event.target.value
        const {setLogin} = this.props

        setLogin(login)

        this.alert()
    }

    createUserHandler = event => {
        const {saveNewUser, login} = this.props

        saveNewUser(login)
            .then(() => this.alert('success', `Usuário ${login} criado com sucesso.`))
            .catch(({response}) => this.alert('warning', response.data))
    }

    render() {
        const {alert} = this.state

        return (
            <form className="form-signin" onSubmit={this.loginFormSubmitHandler} >
                <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
    
                <input id="inputEmail" className="form-control" placeholder="User Login" required autoFocus onChange={this.loginChangeHandler} />

                <div className="checkbox mb-3"/>

                <div className="checkbox mb-3">
                    <button className="btn btn-lg btn-primary btn-block" type="submit">Login</button>
                    <button className="btn btn-lg btn-secondary btn-block" type="button" onClick={this.createUserHandler}>Create</button>
                </div>
                
                <Alert type={alert.type} message={alert.message}/>
            </form>
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
    const {loginModel} = dispatchers

    return {
        setLogin: loginModel.setLogin,
        doLogin: loginModel.doLogin,
        saveNewUser: loginModel.saveNewUser,
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Login)