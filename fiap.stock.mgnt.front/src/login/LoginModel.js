import {fetchUserIdByLogin, postNewUser} from './LoginDomain'

const defaultUser = {
    login: null,
    type: null,
    userId: null
}

const loginModel = {
    state: defaultUser,
    reducers: {
        setLogin(currentState, login) {
            return {...currentState, login}
        },
        setUserId(currentState, userId) {
            console.log("setUserId;", userId)
            return {...currentState, userId}
        }
    },
    effects: {
        async doLogin(login) {
            return fetchUserIdByLogin(login)
                .then(userId => {
                    this.setUserId(userId)

                    return userId
                })
        },
        async saveNewUser(login) {
            return postNewUser(login)
                .then(userId => {
                    this.setUserId(userId)
                    
                    return userId
                })
        }
    }
}

export default loginModel