package com.example.cs492finalproject

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Path
import java.io.Serializable

// Define data classes to represent the API response structure
@JsonClass(generateAdapter = true)
data class DailyMedResults(
    @Json(name = "data") val data: List<DrugInfo>
) : Serializable

@Entity
@JsonClass(generateAdapter = true)
data class DrugInfo(
    @PrimaryKey
    @Json(name = "title") val title: String,
    @Json(name = "spl_version") val splVersion: Int,
    @Json(name = "published_date") val publishedDate: String,
    @Json(name = "setid") val setId: String
) : Serializable

@JsonClass(generateAdapter = true)
data class Packaging(
    @Json(name = "data") val data: PackagingData
) : Serializable

@JsonClass(generateAdapter = true)
data class PackagingData(
    @Json(name = "products") val products: List<PackagingDataProducts>,
    @Json(name = "setid") val setid: String,
    @Json(name = "title") val title: String
) : Serializable

@JsonClass(generateAdapter = true)
data class PackagingDataProducts(
    @Json(name = "product_name_generic") val productNameGeneric: String,
    @Json(name = "product_name") val productName: String,
    @Json(name = "product_code") val productCode: String,
    @Json(name = "packaging") val packaging: List<PackagingDataProductsPackaging>,
    @Json(name = "active_ingredients") val activeIngredients: List<PackagingDataProductsActiveIngredients>,
) : Serializable

@JsonClass(generateAdapter = true)
data class PackagingDataProductsPackaging(
    @Json(name = "ndc") val ndc: String,
    @Json(name = "package_descriptions") val packageDescriptions: List<String>,
) : Serializable

@JsonClass(generateAdapter = true)
data class PackagingDataProductsActiveIngredients(
    @Json(name = "strength") val strength: String,
    @Json(name = "name") val name: String,
) : Serializable

//data class Metadata(
//    @Json(name = "db_published_date") val dbPublishedDate: String,
//    @Json(name = "elements_per_page") val elementsPerPage: Int,
//    @Json(name = "current_url") val currentUrl: String,
//    @Json(name = "next_page_url") val nextPageUrl: String?,
//    @Json(name = "total_elements") val totalElements: Int,
//    @Json(name = "total_pages") val totalPages: Int,
//    @Json(name = "current_page") val currentPage: Int,
//    @Json(name = "previous_page") val previousPage: Any?,
//    @Json(name = "previous_page_url") val previousPageUrl: Any?,
//    @Json(name = "next_page") val nextPage: Int
//)

// Define Retrofit service interface
interface DailyMedService {

    @GET("spls.json")
    suspend fun getDrugData(
        @Query("pagesize") pageSize: Int = 100,
        @Query("page") page: Int = 1,
        @Query("drug_name") drugName: String
    ): Response<DailyMedResults>

    @GET("spls/{SETID}/packaging.json")
    suspend fun getPackagingData(
        @Path("SETID") setid: String,
        @Query("pagesize") pageSize: Int = 100,
        @Query("page") page: Int = 1
    ): Response<Packaging>

    companion object {
        private const val BASE_URL = "https://dailymed.nlm.nih.gov/dailymed/services/v2/"
        fun create(): DailyMedService {
//            val requestUrl = buildRequestUrl("spls.json", drugName = "tylenol")
//            Log.d("SearchQuery", "Request URL: $requestUrl")

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(DailyMedService::class.java)
        }
//        private fun buildRequestUrl(endpoint: String, pageSize: Int = 10, page: Int = 1, drugName: String): String {
//            return BASE_URL.toHttpUrlOrNull()!!.newBuilder()
//                .addPathSegment(endpoint)
//                .addQueryParameter("pagesize", pageSize.toString())
//                .addQueryParameter("page", page.toString())
//                .addQueryParameter("drug_name", drugName)
//                .build()
//                .toString()
//        }
    }
}
// https://dailymed.nlm.nih.gov/dailymed/services/v2/spls.json
//
//// Create a Retrofit instance
//val retrofit = Retrofit.Builder()
//    .baseUrl("https://dailymed.nlm.nih.gov/dailymed/services/v2/")
//    .addConverterFactory(MoshiConverterFactory.create())
//    .build()
//
//// Create an instance of the service interface
//val service = retrofit.create(DailyMedService::class.java)