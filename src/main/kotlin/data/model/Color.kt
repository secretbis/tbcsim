package data.model

enum class Color(val mask: Int) {
    META(1),

    RED(2),
    YELLOW(4),
    BLUE(8),

    ORANGE(6),
    PURPLE(10),
    GREEN(12);
}
