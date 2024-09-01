package com.landa.currencyconverter.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.landa.currencyconverter.data.ApiCurrencyRepository
import com.landa.currencyconverter.domain.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

class MainViewModel @Inject constructor(private val apiCurrencyRepository: ApiCurrencyRepository) : ViewModel() {

    private val _allCurrencies = MutableStateFlow(listOf(""))
    val allCurrencies = _allCurrencies.asStateFlow()
    private val _allCurrenciesShortCut = MutableStateFlow(listOf<String>())
    val allCurrenciesShortCut = _allCurrenciesShortCut.asStateFlow()

    init {
        viewModelScope.launch {
            _allCurrencies.value = apiCurrencyRepository.getCurrenciesList()
        }
    }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    private val currentDate = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val _exchangeDateFlow = MutableStateFlow(currentDate.format(formatter).toString())
    val exchangeDateFlow = _exchangeDateFlow.asStateFlow()

    private val _fromCurrencyStringFlow = MutableStateFlow("EUR")
    val fromCurrencyStringFlow = _fromCurrencyStringFlow.asStateFlow()

    private val _toCurrenciesEmptyListFlow = MutableStateFlow(listOf("All"))
    val toCurrenciesEmptyListFlow = _toCurrenciesEmptyListFlow.asStateFlow()

    private val _toCurrenciesListFlow = MutableStateFlow(listOf<String>())
    val toCurrenciesListFlow = _toCurrenciesListFlow.asStateFlow()

    private val _amountStringFlow = MutableStateFlow("1")
    val amountStringFlow = _amountStringFlow.asStateFlow()

    private val exchangeCurrency = mutableStateOf(Currency(1, "", "", mapOf()))

    private val _resultsListFlow = MutableStateFlow(listOf<String>())
    val resultsListFlow = _resultsListFlow.asStateFlow()

    fun userPickDate(date: String) {
        _exchangeDateFlow.value = date
    }

    fun userPickFromCurrency(currency: String) {
        _fromCurrencyStringFlow.value = currency
    }

    fun userPickToCurrencies(currency: String) {
        val list = _toCurrenciesListFlow.value.toMutableList()
        if (list.contains(currency)) list.remove(currency)
        else list.add(currency)
        _toCurrenciesListFlow.value = list.toList()
    }

    fun userPickAmount(amount: String) {
        val amountInt = amount.toIntOrNull()
        if (amountInt == null) return
        else _amountStringFlow.value = amount
    }

    fun userClickedConvert() {
        viewModelScope.launch {
            val amountValue = _amountStringFlow.value
            val fromCurrencyValue = fromCurrencyStringFlow.value
            val date = _exchangeDateFlow.value.replace('.', '-')
            val dateForApiValue =
                "${date[6]}${date[7]}${date[8]}${date[9]}${date[2]}${date[3]}${date[4]}${date[5]}${date[0]}${date[1]}"
            viewModelScope.launch {
                exchangeCurrency.value = if (_toCurrenciesListFlow.value.isEmpty()) {
                    _allCurrenciesShortCut.value = _allCurrencies.value.map {
                        it.removeRange(3, it.count())
                    }
                    apiCurrencyRepository.getCurrencyFromExchange(
                        dateForApiValue,
                        amountValue,
                        fromCurrencyValue
                    )
                } else apiCurrencyRepository.getCurrencyFromToExchange(
                    dateForApiValue,
                    amountValue,
                    fromCurrencyValue,
                    _toCurrenciesListFlow.value.joinToString(",")
                )
            }.join()
            _resultsListFlow.value = exchangeCurrency.value.rates.map {
                "${_amountStringFlow.value} ${_fromCurrencyStringFlow.value} = ${String.format("%.2f", (it.value * _amountStringFlow.value.toInt()))} ${it.key}"
            }
        }
    }
}