import React from 'react'
import ProductsTable from './ProductsTable'
import ProductForm from './ProductForm'

const products = props => {
    return (
        <div>
            <ProductForm />
            <ProductsTable />
        </div>
    )
}

export default products