package xyz.eddief.halfway.data.models

data class DistanceResult(
    val destination_addresses : List<String?>?,
    val origin_addresses : List<String?>?,
    val error_message : String?,
    val rows :  List<Row?>?,
    val status : String?
) {

    data class Row(val elements: List<Element>)

    data class Element(val distance: Distance, val duration: Distance, val status: String?)

    data class Distance(val text: String?, val value: String?)
}