package br.bano.chucknorris.ui.joke

class JokeUiData(
        val id: String,
        val joke: String,
        val category: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JokeUiData

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        const val ICON_URL = "https://assets.chucknorris.host/img/chucknorris_logo_coloured_small@2x.png"
    }
}