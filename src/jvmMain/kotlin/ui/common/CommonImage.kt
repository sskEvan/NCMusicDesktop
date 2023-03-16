package ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import org.succlz123.lib.imageloader.ImageAsyncImageFile
import org.succlz123.lib.imageloader.ImageAsyncImageUrl
import org.succlz123.lib.imageloader.ImageRes
import org.succlz123.lib.imageloader.core.ImageCallback
import util.StringUtil


@Composable
fun AsyncImage(
    modifier: Modifier,
    url: String?,
    placeHolderUrl: String? = "image/ic_disk_place_holder.webp",
    errorUrl: String? = "image/ic_disk_place_holder.webp",
    contentScale: ContentScale = ContentScale.Crop
) {
    val url = url ?: "image/ic_disk_place_holder.webp"
    if (url.startsWith("http")) {
        ImageAsyncImageUrl(url = url, imageCallback = ImageCallback(placeHolderView = {
            placeHolderUrl?.let {
                Image(
                    painter = painterResource(placeHolderUrl),
                    contentDescription = url,
                    modifier = modifier,
                    contentScale = ContentScale.Inside
                )
            }
        }, errorView = {
            errorUrl?.let {
                Image(
                    painter = painterResource(errorUrl),
                    contentDescription = url,
                    modifier = modifier,
                    contentScale = contentScale
                )
            }
        }) {
            Image(
                painter = it, contentDescription = url, modifier = modifier, contentScale = contentScale
            )
        })
    } else if (url.startsWith("/")) {
        ImageAsyncImageFile(filePath = url, imageCallback = ImageCallback(placeHolderView = {
            placeHolderUrl?.let {
                Image(
                    painter = painterResource(placeHolderUrl),
                    contentDescription = url,
                    modifier = modifier,
                    contentScale = ContentScale.Inside
                )
            }
        }, errorView = {
            errorUrl?.let {
                Image(
                    painter = painterResource(errorUrl),
                    contentDescription = url,
                    modifier = modifier,
                    contentScale = contentScale
                )
            }
        }) {
            Image(
                painter = it, contentDescription = url, modifier = modifier, contentScale = contentScale
            )
        })
    } else {
        ImageRes(resName = url, imageCallback = ImageCallback(placeHolderView = {
            placeHolderUrl?.let {
                Image(
                    painter = painterResource(placeHolderUrl),
                    contentDescription = url,
                    modifier = modifier,
                    contentScale = ContentScale.Inside
                )
            }
        }, errorView = {
            errorUrl?.let {
                Image(
                    painter = painterResource(errorUrl),
                    contentDescription = url,
                    modifier = modifier,
                    contentScale = contentScale
                )
            }
        }) {
            Image(
                painter = it, contentDescription = url, modifier = modifier, contentScale = contentScale
            )
        })
    }
}