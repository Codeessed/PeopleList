package com.example.interview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NameAdapter

    // ── Sample data ───────────────────────────────────────────────────────────
    private val people = listOf(
        Person("Alice Johnson", "Product Designer"),
        Person("Bob Martinez", "Software Engineer"),
        Person("Catherine Lee", "Data Scientist"),
        Person("David Kim", "DevOps Engineer"),
        Person("Emily Chen", "UX Researcher"),
        Person("Frank Okafor", "Backend Developer"),
        Person("Grace Mensah", "Mobile Engineer"),
        Person("Henry Patel", "QA Engineer"),
        Person("Isabella Torres", "Scrum Master"),
        Person("James Nakamura", "Frontend Developer"),
        Person("Karen Adeyemi", "CTO"),
        Person("Liam O'Brien", "Cloud Architect"),
        Person("Maya Sharma", "Machine Learning Engineer"),
        Person("Nathan Brooks", "Security Analyst"),
        Person("Olivia Nguyen", "Product Manager"),
        Person("Paul Andersen", "Database Administrator"),
        Person("Quinn Dumont", "UI Designer"),
        Person("Rachel Ivanova", "iOS Developer"),
        Person("Sebastian Cruz", "Android Developer"),
        Person("Tina Yamamoto", "Tech Lead")
    )
    // ─────────────────────────────────────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        updateCount(people.size)
    }

    private fun setupRecyclerView() {
        adapter = NameAdapter(people) { count -> updateCount(count) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = null   // avoid flicker during filtering
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                adapter.filter.filter(query)

                // Show/hide clear button
                binding.ivClear.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })

        binding.ivClear.setOnClickListener {
            binding.etSearch.text?.clear()
            hideKeyboard()
        }
    }

    private fun updateCount(count: Int) {
        val isEmpty = count == 0 && binding.etSearch.text?.isNotEmpty() == true
        binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE

        binding.tvCount.text = if (count == people.size) {
            "${people.size} people"
        } else {
            "$count of ${people.size} found"
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}