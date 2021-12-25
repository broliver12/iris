import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strasz.colorpicker.database.ColorModel
import com.strasz.colorpicker.util.ColorUtil
import kotlinx.android.synthetic.main.view_fav_color_tile.view.*

class ColorTileViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private var showAllInfo = false
    fun bind(model: ColorModel){
        showAllInfo = false
        view.rootView.setOnClickListener {
            if(showAllInfo){
                showAllInfo = false
                view.tileInfoContainer.visibility = View.GONE
                view.tileColorView.visibility = View.VISIBLE
            } else {
                showAllInfo = true
                view.tileColorView.visibility = View.INVISIBLE
                view.tileInfoContainer.visibility = View.VISIBLE
            }
        }
        view.tileColorView.setBackgroundColor(model.hexVal)
        view.tileHexTextView.text = ColorUtil.generateHexString(model.hexVal)
        view.tileRGBTextView.text = "RGB: \n" + ColorUtil.generateRGBString(model.hexVal)
        view.tileCMYKTextView.text = "CMYK: \n" + ColorUtil.generateCMYKString(model.hexVal)
        view.tileInfoContainer.visibility = View.GONE
    }
}