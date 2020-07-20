import React, { Component } from 'react'
import {connect} from 'react-redux'

export class Alert extends Component {

    constructor(props) {
        super(props)

        this.state = {
        }
    }
    
    componentDidMount() {
    }

    render() {
        const {type, message} = this.props

        const style = {
            display: message ? 'block' : 'none'
        }

        const className = `mb-3 alert alert-${type}`

        return (
            <div className={className} role="alert" style={style}>
                {message}
            </div>
        )
    }
}

export default connect(
    state => {
        const {alertModel} = state

        return {
            type: alertModel.type,
            message: alertModel.message
        }
    },
    dispatchers => {

        return {
        }
    }
)(Alert)