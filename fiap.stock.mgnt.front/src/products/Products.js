import React from 'react'
import ProductsTable from './ProductsTable'
import ProductForm from './ProductForm'
import Alert from '../alert/Alert'

const products = props => {
    return (
        <div>
            <ProductForm />

            <Alert />

            <ProductsTable />
        </div>
    )
}

export default products