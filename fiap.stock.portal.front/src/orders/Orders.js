import React from 'react'
import OrdersTable from './OrdersTable'
import OrderForm from './OrderForm'

const orders = props => {
    return (
        <div>
            <OrderForm />

            <OrdersTable />
        </div>
    )
}

export default orders