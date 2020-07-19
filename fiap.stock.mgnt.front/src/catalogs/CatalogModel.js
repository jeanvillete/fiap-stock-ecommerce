import { fetchCatalogItems, postNewCatalogItem } from './CatalogDomain'

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
                .then(() => this.findAll())
        }
    }
}

export default catalogModel