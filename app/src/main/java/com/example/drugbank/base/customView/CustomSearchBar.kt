import com.example.drugbank.R
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.drugbank.databinding.CustomSearchBarBinding
import com.example.drugbank.util.extension.hindSoftKeyboard
import com.example.drugbank.util.extension.showSoftKeyboard


class CustomSearchBar(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    val binding: CustomSearchBarBinding

    init {
        val attr = context.obtainStyledAttributes(attributeSet, R.styleable.CustomSearchBar)
        val textHint = attr.getString(R.styleable.CustomSearchBar_android_hint) ?: ""

        attr.recycle()
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_search_bar, this, true)
        setTextHint(textHint)
        binding.etSearch.apply {
            binding.ivBtnSearch.setOnClickListener {
                this.isFocusableInTouchMode = true
                this.isFocusable = true
                this.requestFocus()
                this.showSoftKeyboard(context)
            }
            binding.etSearch.setOnClickListener {
                this.isFocusableInTouchMode = true
                this.isFocusable = true
                this.requestFocus()
                this.showSoftKeyboard(context)
            }
            binding.ivBtnClose.setOnClickListener {
                this.setText("")
                this.isFocusable = false
                this.clearFocus()
                this.hindSoftKeyboard(context)
            }
        }

        binding.etSearch.onFocusChangeListener  = OnFocusChangeListener {v,hasFocus ->
            if(hasFocus) {
                binding.ivBtnClose.visibility = View.VISIBLE
            }else {
                binding.ivBtnClose.visibility = View.INVISIBLE
            }
        }
    }

    fun setTextHint(text: String) {
        binding.etSearch.hint = text
    }

    fun onSearchQuery(listener: (String) -> Unit) {
        binding.etSearch.doAfterTextChanged {it ->
            listener.invoke(it.toString())
        }
    }

    fun addSuggestionArr(arr: Array<String>){
        val arrAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, arr)
        binding.etSearch.setAdapter(arrAdapter)
    }


    fun onSuggestionItemSelected(listener: AdapterView.OnItemSelectedListener) {
        binding.etSearch.onItemSelectedListener = listener
    }
}
