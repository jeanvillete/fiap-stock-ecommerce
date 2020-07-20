import React from 'react'
import OrdersTable from './OrdersTable'
import Alert from '../alert/Alert'

const orders = props => {
    return (
        <div>
            <Alert />
            
            <OrdersTable />
        </div>
    )
}

export default orders