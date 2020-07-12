import React from 'react'

const alert = props => {

    const {type, message} = props

    const style = {
        display: message ? 'block' : 'none'
    }

    const className = `mb-3 alert alert-${type}`

    console.log('Alert; ', props)

    return (
        <div className={className} role="alert" style={style}>
            {message}
        </div>
    )
}

export default alert