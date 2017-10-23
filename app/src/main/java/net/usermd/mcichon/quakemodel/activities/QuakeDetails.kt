package net.usermd.mcichon.quakemodel.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView

import android.widget.SimpleAdapter
import net.usermd.mcichon.quakemodel.R
import java.util.ArrayList


class QuakeDetails : AppCompatActivity(){

    private var listView: ListView? = null
    private val result = ArrayList<java.util.HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quake_details)

        listView = findViewById<View>(R.id.listView) as ListView

        val bundle = intent.extras

        val repo: HashMap<String, String> = HashMap();

        if (bundle != null) {
            repo.put("title", bundle.getString("title"))
            repo.put("place", "place: " + bundle.getString("place"))
            repo.put("time", "time: " + bundle.getFloat("time").toString())
            repo.put("url", "url: " + bundle.getString("url"))
            repo.put("mag", "mag: " + bundle.getFloat("mag").toString())
            repo.put("coordinates", "coordinates: 1")
        }

        result.add(repo)

        val listAdapter = SimpleAdapter(
                this@QuakeDetails,
                result,
                R.layout.activity_quake_list_item,
                arrayOf("title", "place", "time", "url", "mag", "coordinates"),
                intArrayOf(R.id.title, R.id.place, R.id.time, R.id.url, R.id.mag, R.id.coordinates)
        )
        listView!!.adapter = listAdapter
    }
}