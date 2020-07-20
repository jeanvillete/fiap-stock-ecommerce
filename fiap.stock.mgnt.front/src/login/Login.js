import React, { Component } from 'react'
import {connect} from 'react-redux'
import Alert from '../alert/Alert'

import '../signin.css';

export class Login extends Component {

    constructor(props) {
        super(props)

        this.state = {
        }
    }

    componentDidMount() {
        const {clearAlert} = this.props
        clearAlert()
    }

    loginFormSubmitHandler = event => {
        event.preventDefault()

        const {doLogin, login, successAlert, warningAlert} = this.props

        doLogin(login)
            .then(() => successAlert(`Usuário ${login} logado com sucesso.`))
            .catch(({response}) => warningAlert(response.data))
    }

    loginChangeHandler = event => {
        const login = event.target.value;
        const {setLogin, clearAlert} = this.props

        setLogin(login)
        clearAlert()
    }

    createUserHandler = event => {
        const {login} = this.props
        const {saveNewUser, successAlert, warningAlert} = this.props

        saveNewUser(login)
            .then(() => successAlert(`Usuário ${login} criado com sucesso.`))
            .catch(({response}) => warningAlert(response.data))
    }

    render() {
        return (
            <form className="form-signin" onSubmit={this.loginFormSubmitHandler} >
                <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
    
                <input id="inputEmail" className="form-control" placeholder="User Login" required autoFocus onChange={this.loginChangeHandler} />

                <div className="checkbox mb-3"/>

                <div className="checkbox mb-3">
                    <button className="btn btn-lg btn-primary btn-block" type="submit">Login</button>
                    <button className="btn btn-lg btn-secondary btn-block" type="button" onClick={this.createUserHandler}>Create</button>
                </div>
                
                <Alert />
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
    const {loginModel, alertModel} = dispatchers

    return {
        setLogin: loginModel.setLogin,
        doLogin: loginModel.doLogin,
        saveNewUser: loginModel.saveNewUser,
        clearAlert: alertModel.clear,
        successAlert: alertModel.success,
        warningAlert: alertModel.warning
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Login)