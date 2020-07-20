const emptyAlert = () => {
    return {
        type: null, 
        message: null
    }
}

const defaultState = emptyAlert()

const alertModel = {
    state: defaultState,
    reducers: {
        clear(currentState){
            const {type, message} = emptyAlert()
            return {
                ...currentState, 
                type, 
                message
            }
        },
        success(currentState, message){
            return {
                ...currentState, 
                type: 'success',
                message: message
            }
        },
        warning(currentState, message){
            return {
                ...currentState, 
                type: 'warning',
                message: message
            }
        }
    },
    effects: {
    }
}

export default alertModel