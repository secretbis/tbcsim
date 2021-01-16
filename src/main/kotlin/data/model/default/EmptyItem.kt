package data.model.default

import data.model.Item

class EmptyItem : Item(
    id = -1,
    procs = listOf()
) {
}
