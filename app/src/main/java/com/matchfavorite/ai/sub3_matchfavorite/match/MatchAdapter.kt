package com.matchfavorite.ai.sub3_matchfavorite.match

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.matchfavorite.ai.sub3_matchfavorite.model.Match
import com.matchfavorite.ai.sub3_matchfavorite.util.parseDate
import org.jetbrains.anko.*

class MatchAdapter(private val items: MutableList<Match>, private val listener: (Match) -> Unit)
    : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(ClubItem().createView(AnkoContext.create(parent.context, parent)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val clubHome = view.findViewById<TextView>(ClubItem.clubHome)
        private val scoreHome = view.findViewById<TextView>(ClubItem.scoreHome)
        private val clubAway = view.findViewById<TextView>(ClubItem.clubAway)
        private val scoreAway = view.findViewById<TextView>(ClubItem.scoreAway)
        private val textDate = view.findViewById<TextView>(ClubItem.textDate)

        fun bindItem(items: Match, listener: (Match) -> Unit) {
            textDate.text = parseDate(items.matchDate, items.matchTime)
            clubHome.text = items.teamHomeName
            clubAway.text = items.teamAwayName
            scoreHome.text = items.teamHomeScore
            scoreAway.text = items.teamAwayScore

            itemView.setOnClickListener {
                listener(items)
            }
        }
    }
    class ClubItem() : AnkoComponent<ViewGroup> {
        companion object {
            val textDate = 1
            val clubHome = 2
            val clubAway = 3
            val scoreHome = 4
            val scoreAway = 5
        }

        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            return linearLayout {
                lparams(width = matchParent, height = wrapContent) {
                    topMargin = dip(8)
                    bottomMargin = dip(8)
                }
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                backgroundColor = Color.WHITE
                verticalPadding = dip(12)

                textView {
                    id = textDate
                    padding = dip(4)
                    textSize = 16f
                    typeface = Typeface.DEFAULT_BOLD
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER
                }

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                    lparams(width = matchParent, height = wrapContent) {
                        topMargin = dip(4)
                        bottomMargin = dip(4)
                    }

                    textView {
                        id = clubHome
                        textSize = 16f
                        gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    }.lparams(width = matchParent, height = matchParent){
                        weight = 1F
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER
                        horizontalPadding = dip(16)
                        textView {
                            id = scoreHome
                            textSize = 18f
                            typeface = Typeface.DEFAULT_BOLD
                            gravity = Gravity.CENTER
                        }.lparams(width = wrapContent, height = wrapContent)
                        textView {
                            text = " - "
                            horizontalPadding = dip(3)
                            textSize = 16f
                            typeface = Typeface.DEFAULT_BOLD
                            gravity = Gravity.CENTER
                        }.lparams(width = wrapContent, height = wrapContent)
                        textView {
                            id = scoreAway
                            textSize = 18f
                            typeface = Typeface.DEFAULT_BOLD
                            gravity = Gravity.CENTER
                        }.lparams(width = wrapContent, height = wrapContent)
                    }.lparams(width = wrapContent, height = matchParent){
                        weight = 0F
                    }

                    textView {
                        id = clubAway
                        textSize = 16f
                        gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    }.lparams(width = matchParent, height = matchParent) {
                        weight = 1F
                    }

                }
            }
        }
    }
}