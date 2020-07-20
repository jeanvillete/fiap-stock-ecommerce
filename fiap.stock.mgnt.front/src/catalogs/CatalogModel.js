import { fetchCatalogItems, postNewCatalogItem, putCatalogItem, deleteCatalogItem } from './CatalogDomain'

const defaultState = {
    catalogs: []
}

const catalogModel = {
    state: defaultState,
    reducers: {
        setCatalogs(currentState, catalogs) {
            return {...currentState, catalogs}
        }
    },
    effects: {
        async findAll(login) {
            return fetchCatalogItems(login)
                .then(catalogs => {
                    this.setCatalogs(catalogs)

                    return catalogs
                })
        },
        async saveNewCatalogItem({userId, description}) {
            return postNewCatalogItem(userId, description)
                .then(catalogItem => {
                    this.findAll()

                    return catalogItem
                })
        },
        async updateCatalogItem({userId, catalog}) {
            return putCatalogItem(userId, catalog)
                .then(catalogItem => {
                    this.findAll()

                    return catalogItem
                })
        },
        async deleteCatalogItemById({userId, catalogId}) {
            return deleteCatalogItem(userId, catalogId)
                .then(() => this.findAll())
        }
    }
}

export default catalogModel