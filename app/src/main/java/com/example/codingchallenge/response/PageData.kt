package com.example.codingchallenge.response

import java.io.Serializable


data class PageData(val page: Page) : Serializable

data class Page(val cards: List<Cards>)

data class Cards (val card_type: String, val card: Card)

data class Card(val title: CardAttributes, val description: CardAttributes, val image: Image, val value: String, val attributes: Attributes)

data class Attributes(val text_color: String, val font: Font)

data class Font(val size: Float)

data class Image(val url: String, val size: ImageSize)

data class ImageSize(val width: Int, val height: Int)

data class CardAttributes(val value: String, val attributes: Attributes)