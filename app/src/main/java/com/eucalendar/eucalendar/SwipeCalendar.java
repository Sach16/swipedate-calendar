package com.eucalendar.eucalendar;

/**
 * Created by S.K. Pissay on 2/4/16.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by S.K. Pissay on 12/3/2015.
 */
public class SwipeCalendar extends AppCompatActivity implements SlotSelectListner, View.OnClickListener, View.OnTouchListener {

    private static final String tag = "MyCalendarActivity";

    private float x1, x2;
    private int m_cRowId;
    static final int MIN_DISTANCE = 50;
    private boolean SWIPED = false;
    ArrayList<Integer> m_cPosn;
    ArrayList<Integer> m_cPreviousPos;
    private Calendar mCalendar;

    private LinearLayout m_cSwipeLay;
    private TextView currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private CalendarGridView calendarView;
    private GridCellAdapter adapter;
    private Calendar m_cObjCalendar;
    private HashMap<String, String> m_cAbsents;

    private final int[] m_cDaysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private int m_cMonth;
    private int m_cYear;
    private int m_cToday_Date;
    private int m_cMonth_Now;
    private int m_cYear_Now;

    private final String[] m_cDaysForWeeks = new String[]{"Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday"};

    private final String[] m_cMonths = {"Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"};


    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    //    CustomCalendarView m_cCalanderView;
    /*CalendarGridView m_cMrngGridSlots;
    TextView m_cMrngText;

    TextView m_cBtnSchedule;
    TextView m_cDoctorAdv;
    TextView m_cSuggestion;
    TextView m_cTimeOfSuggestion;*/

    ArrayList<String> m_cDayList;
    //    ArrayList<String> m_cMrngList, m_cNoonList, m_cEvengList;
    String m_cSelectedDate;
    String m_cTitle;
    int m_cConsulType;
    String m_cDoctorId;
    int m_cDate;
    String m_cMappingId;

    public CalenderSqureButton m_cSelectedView;

    private String m_cCaseId;
    private String m_cParentID;
    private String m_cAppointmentType;
    private String m_cPackageID;
    private boolean m_cISFROMMYPACKAGE;

    @Override
    protected void onCreate(Bundle pSavedInstance) {
        super.onCreate(pSavedInstance);
        setContentView(R.layout.activity_euremedies_appointment_schduling);
        // Slider Initilization


        m_cTitle = getIntent().getStringExtra("TITLE_NAME");

        m_cMappingId = getIntent().getStringExtra("MAPPINGID");
        m_cDoctorId = getIntent().getStringExtra("DOCTORID");
        m_cCaseId = getIntent().getStringExtra("CASE_ID");
        m_cParentID = getIntent().getStringExtra("ParentID");
        m_cAppointmentType = getIntent().getStringExtra("AppointmentType");
        m_cPackageID = getIntent().getStringExtra("PACKAGEID");
        m_cISFROMMYPACKAGE = getIntent().getBooleanExtra("MYPACKAGE", false);
        String lAppoinmentId = getIntent().getStringExtra("APPOINTMENTID");
        String lOfferID = getIntent().getStringExtra("OFFER_ID");


        if (null != lOfferID) {
            m_cAppointmentType = "5";
        }

        m_cObjCalendar = Calendar.getInstance(Locale.getDefault());
        m_cMonth = m_cObjCalendar.get(Calendar.MONTH);
        m_cYear = m_cObjCalendar.get(Calendar.YEAR);
        m_cAbsents = new HashMap<>();
        m_cAbsents.put("2016-04-14", "2016-04-14");
        m_cAbsents.put("2016-04-20", "2016-04-20");
        init();
        initlizationCalender();
    }

    private void Todaysis() {
        m_cObjCalendar = Calendar.getInstance(Locale.getDefault());
        m_cToday_Date = m_cObjCalendar.get(Calendar.DAY_OF_MONTH);
        m_cMonth_Now = m_cObjCalendar.get(Calendar.MONTH);
        m_cYear_Now = m_cObjCalendar.get(Calendar.YEAR);
    }

    private void init() {
        //Initialize CustomCalendarView from layout
        m_cSwipeLay = (LinearLayout) findViewById(R.id.ATENDANCE_LAY);
        m_cSwipeLay.setOnClickListener(this);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        m_cSelectedDate = df.format(new Date());
        /*m_cMrngGridSlots = (CalendarGridView) findViewById(R.id.MORNING_GRID_SLOTS);

        m_cBtnSchedule = (TextView) findViewById(R.id.BTN_SCHEDULE);
        m_cTimeOfSuggestion = (TextView) findViewById(R.id.TXT_SCHDULE);
        m_cSuggestion = (TextView) findViewById(R.id.TXT_SUGGESTION);
        m_cDoctorAdv = (TextView) findViewById(R.id.TEXT_ADV_SCHDULE);

        m_cMrngText = (TextView) findViewById(R.id.MORNING_TEXT);

        m_cMrngText.setOnClickListener(this);
        m_cBtnSchedule.setOnClickListener(this);

        m_cTimeOfSuggestion.setText("Schedule a 15 mins " + m_cTitle + " call with the Doctor.");
        m_cSuggestion.setText("Select a available slot for " + m_cTitle + " Appointment");
*/
    }

    private void updateUIbyDate() {
        m_cDayList = new ArrayList<>();
        updateGrid();
    }


    private void updateGrid() {
        if (null != m_cDayList && m_cDayList.size() > 0) {
            SlotAdapter lMrngSlot = new SlotAdapter(this, m_cDayList, this);
            double size = m_cDayList.size() / 4.9;
//            double dp = (int)(360/this.getResources().getDisplayMetrics().density);
            final float scale = this.getResources().getDisplayMetrics().density;
            int pixels = (int) (79 * scale + 0.5f);
            double dp = pixels * (size + 0.5f);
            if (m_cDayList.size() <= 5) {
                dp = pixels * (size + 1);
            }
//            m_cMrngGridSlots.setLayoutParams(new LinearLayout.LayoutParams(CalendarGridView.AUTO_FIT, (int) dp));

//            m_cMrngGridSlots.setAdapter(lMrngSlot);
        }
        /*if(null != m_cNoonList && m_cNoonList.size() > 0) {
            SlotAdapter lNoonSlot = new SlotAdapter(this, m_cNoonList);
            int size = m_cNoonList.size()/3;
            int dp = (int)(320/this.getResources().getDisplayMetrics().density);
            dp = dp*size;
            m_cNoonGridSlots.setLayoutParams(new CalendarGridView.LayoutParams(CalendarGridView.AUTO_FIT, dp));
            m_cNoonGridSlots.setAdapter(lNoonSlot);
            m_cNoonGridSlots.setOnItemClickListener(this);
        }
        if(null != m_cEvengList && m_cEvengList.size() > 0) {
            SlotAdapter lEvengSlot = new SlotAdapter(this, m_cEvengList);
            int size = m_cEvengList.size()/3;
            int dp = (int)(320/this.getResources().getDisplayMetrics().density);
            dp = dp*size;
            m_cEvngGridSlots.setLayoutParams(new CalendarGridView.LayoutParams(CalendarGridView.AUTO_FIT, dp));
            m_cEvngGridSlots.setAdapter(lEvengSlot);
            m_cEvngGridSlots.setOnItemClickListener(this);
        }*/
    }

    @Override
    public void onSlotClickedListner(String pTimeSlot) {
        /*m_cObjAppoinmentTable.setStatus("2");
        String lStringDate = EURemediesMacros.getDateFormat(null, m_cSelectedDate, EURemediesMacros.DEFAULT_DATEFORMAT_YYYYMMDD, EURemediesMacros.DATE_FORMAT_UNDERSC_YYYYMMDD_HHMMSS_T);
        m_cObjAppoinmentTable.setAppointmentDate(lStringDate);
        m_cObjAppoinmentTable.setConsultationType(m_cConsulType + "");
        m_cObjAppoinmentTable.setFromTime(pTimeSlot);
        String[] FromDate = pTimeSlot.split(":");
        String ToDate;
        if(FromDate[1].contains("15")) {
            ToDate = FromDate[0]+":30";
        } else if(FromDate[1].contains("30")) {
            ToDate = FromDate[0]+":45";
        } else if(FromDate[1].contains("45")){
            int Number = Integer.valueOf(FromDate[0]);
            ToDate = (Number+1)+":00";
        } else {
            ToDate = FromDate[0]+":15";
        }
        m_cObjAppoinmentTable.setToTime(ToDate + ":00");
        m_cObjAppoinmentTable.setCaseID(m_cCaseId);
        m_cObjAppoinmentTable.setPatientID(m_cPatientID);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent lIntent;
        String[] lSelectedDate;
        switch (v.getId()) {
            case R.id.prevMonth:
                Calendar lCal = Calendar.getInstance();
                int lCurrentMonth = lCal.get(Calendar.MONTH);
                int lCurrentYear = lCal.get(Calendar.YEAR);
                boolean lisAction = false;
                if (m_cYear > lCurrentYear) {
                    lisAction = true;
                } else if (m_cYear == lCurrentYear && m_cMonth > lCurrentMonth) {
                    lisAction = true;
                }
                if (lisAction) {
                    if (m_cMonth <= 0) {
                        m_cMonth = 11;
                        m_cYear--;
                    } else {
                        m_cMonth--;
                    }
                    setGridCellAdapterToDate(m_cMonth, m_cYear);
//                    m_cMrngGridSlots.setVisibility(View.GONE);
                }
                break;
            case R.id.nextMonth:

                if (m_cMonth >= 11) {
                    m_cMonth = 0;
                    m_cYear++;
                } else {
                    m_cMonth++;
                }
                setGridCellAdapterToDate(m_cMonth, m_cYear);
//                m_cMrngGridSlots.setVisibility(View.GONE);
                break;
            case R.id.currentMonth:
                datePicker();
                break;
            case R.id.ATENDANCE_LAY:
                lIntent = new Intent(this, CalendarActivity.class);
                startActivity(lIntent);
                break;
        }
    }

    private void datePicker() {

        mCalendar = Calendar.getInstance();

        final View dialogView = View.inflate(this, R.layout.month_picker_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

        datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), null);


        LinearLayout ll = (LinearLayout) datePicker.getChildAt(0);
        LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
        ll2.getChildAt(1).setVisibility(View.GONE);

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                mCalendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth());

                alertDialog.dismiss();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();
    }


    private void initlizationCalender() {
        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setOnClickListener(this);
        currentMonth.setText(DateFormat.format(dateTemplate, m_cObjCalendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (CalendarGridView) this.findViewById(R.id.calendar);
        calendarView.setOnTouchListener(this);

        // Initialised
        adapter = new GridCellAdapter(this, m_cMonth, m_cYear);
        calendarView.setAdapter(adapter);
    }


    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(this, month, year);
        m_cObjCalendar.set(year, month, 1);
        currentMonth.setText(DateFormat.format(dateTemplate, m_cObjCalendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        float currentXPosition = -1;
        float currentYPosition = -1;
        int position = -1;
        LinearLayout numbLay = null;

        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                m_cPreviousPos = m_cPosn;
                x1 = me.getX();
                m_cPosn = new ArrayList<>();
                currentXPosition = me.getX();
                currentYPosition = me.getY();
                position = calendarView.pointToPosition((int) currentXPosition, (int) currentYPosition);
                m_cRowId = position / calendarView.getNumColumns();
                m_cPosn.add(position);
                break;
            case MotionEvent.ACTION_MOVE:
                currentXPosition = me.getX();
                currentYPosition = me.getY();
                position = calendarView.pointToPosition((int) currentXPosition, (int) currentYPosition);
                int lRow = position / calendarView.getNumColumns();
//                if (m_cRowId == lRow) {
                m_cPosn.add(position);
//                }
                if (null != m_cPreviousPos) {
                    //TODO remove swiped pos and also cleans all pos on the grid
                    for (int i = 0; i < adapter.getGridSize(); i++) {
                        EachDate lItem = adapter.getGridItem(i);
                        if (null != lItem) {
                            lItem.m_cColor = "WHITE";
                            adapter.setGridItem(i, lItem);
                        }
                            /*if(null != lItem.m_cContentView) {
                                numbLay = (LinearLayout) lItem.m_cContentView;
                                if (position != -1 && numbLay != null) {
                                    lItem.lColor = Color.WHITE;
                                    numbLay.setBackgroundColor(lItem.lColor);
                                }
                            }*/
                    }
                }

                // TODO add -1 to last index
                int last = m_cPosn.get(m_cPosn.size()-1);
                for (int y = m_cPosn.get(0); y < last ; y++ ) {
//                         numbLay = (LinearLayout) m_cCalendarGridView.getItemAtPosition(position);
                    EachDate lItem = adapter.getGridItem(y);
                    if (null != lItem) {
                        lItem.m_cColor = "RED";
                        adapter.setGridItem(y, lItem);
                    }

                        /*if(null != lItem.m_cContentView) {
                            numbLay = (LinearLayout) lItem.m_cContentView;
                            if (position != -1 && numbLay != null) {
                                lItem.lColor = Color.BLUE;
                                numbLay.setBackgroundColor(lItem.lColor);
                            }
                        }*/
                }
                adapter.notifyDataSetChanged();
                break;
            case MotionEvent.ACTION_UP:
                x2 = me.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
//                    SWIPED = true;
//                    if(m_cPosn.size())
                    int action = me.getActionMasked();  // MotionEvent types such as ACTION_UP, ACTION_DOWN
                    currentXPosition = me.getX();
                    currentYPosition = me.getY();
                    position = calendarView.pointToPosition((int) currentXPosition, (int) currentYPosition);
                    int lRow1 = position / calendarView.getNumColumns();
//                    if (m_cRowId == lRow1) {
                        m_cPosn.add(position);
//                    }
                    // Access text in the cell, or the object itself

                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
//        if(SWIPED) {
        /*int action = me.getActionMasked();  // MotionEvent types such as ACTION_UP, ACTION_DOWN
        float currentXPosition = me.getX();
        float currentYPosition = me.getY();
        int position = m_cCalendarGridView.pointToPosition((int) currentXPosition, (int) currentYPosition);

        // Access text in the cell, or the object itself
        LinearLayout numbLay = (LinearLayout) m_cCalendarGridView.getChildAt(position);
        if (position != -1 && numbLay != null) {
            numbLay.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        m_cPosn.add(position);*/
        return false;
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {

        private static final String tag = "GridCellAdapter";
        private final Context m_cContext;
        private final List<EachDate> m_cObjList;
        private static final int DAY_OFFSET = 1;

        private int m_cDaysInMonth;
        //		private int m_cSelectedDay;
        private TextView m_cGridText;
        private RelativeLayout m_cGridCell;
        private final SimpleDateFormat m_cDateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        public GridCellAdapter(Context context, int month, int year) {
            super();
            this.m_cContext = context;
            this.m_cObjList = new ArrayList<EachDate>();

            // Print Month
            printMonth(month, year);
        }

        @Override
        public int getCount() {
            return m_cObjList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return m_cObjList.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            LayoutInflater inflater = (LayoutInflater) m_cContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.screen_gridcell, parent, false);

            // Get a reference to the Day gridcell
            m_cGridText = (TextView) row.findViewById(R.id.CALENDAR_DAY_GRIDTEXT);
            m_cGridCell = (RelativeLayout) row.findViewById(R.id.CALENDAR_DAY_GRIDCELL);
//			LayoutParams lParams = new LayoutParams(lwidth, lwidth);
//			m_cGridText.setLayoutParams(lParams);
//            m_cGridText.setOnClickListener(this);

            // ACCOUNT FOR SPACING

//			String[] day_color = m_cObjList.get(position).m_cDate.split("-");
            EachDate lEachDay = m_cObjList.get(position);
            String[] day = lEachDay.m_cDate.split("-");
//			m_cdayScreenDay = day[0];
//			M_cdayscreenMonth = day[1];
//			m_cdayscreenYear = day[2];

            // Set the Day GridCell
            m_cGridText.setText(day[0]);
            String lDate = m_cObjList.get(position).m_cDate;
            m_cGridText.setTag(lEachDay);
            m_cGridCell.setTag(lEachDay);

            if (lEachDay.m_cColor.equals("GREY")) {
                m_cGridText.setTextColor(Color.LTGRAY);
//                m_cGridText.setEnabled(false);
//                m_cGridText.setClickable(false);
//                m_cGridText.setFocusable(false);

//				m_cGridText.setVisibility(View.INVISIBLE);
//                m_cGridText.setFocusableInTouchMode(false);

            }
//            if (lEachDay.m_cColor.equals("WHITE")) {
//                m_cGridText.setTextColor(Color.BLACK);
//            }
//            if (lEachDay.m_cColor.equals("BLUE")) {
//                m_cGridText.setTextColor(Color.RED);
//            }
//            if (lEachDay.m_cColor.equals("YELLOW")) {
//                m_cGridText.setTextColor(Color.GREEN);
//            }

            if (lEachDay.m_cColor.equals("WHITE")) {
                m_cGridText.setTextColor(Color.BLACK);
                m_cGridText.setBackgroundColor(Color.WHITE);
            }
            if (lEachDay.m_cColor.equals("BLUE")) {
                m_cGridText.setTextColor(Color.parseColor("#05CFB5"));
                m_cGridText.setBackgroundColor(Color.WHITE);
//                m_cGridCell.setBackground(getResources().getDrawable(R.drawable.circle_thin));
            }
            if (lEachDay.m_cColor.equals("YELLOW")) {
                m_cGridText.setTextColor(Color.RED);
                m_cGridText.setBackgroundColor(Color.WHITE);
                m_cGridText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.red_dot);
            }
            if (lEachDay.m_cColor.equals("RED")) {
                m_cGridText.setTextColor(Color.BLACK);
                m_cGridText.setBackgroundColor(Color.RED);
                m_cGridText.setPadding(0, 15, 0, 15);
            }

            return row;
        }

        public EachDate getGridItem(int pPos) {
            try {
                return m_cObjList.get(pPos);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setGridItem(int pPos, EachDate lItem) {
            m_cObjList.set(pPos, lItem);
        }

        public int getGridSize() {
            return m_cObjList.size();
        }


        @Override
        public void onClick(View v) {
            EachDate lTag = (EachDate) v.getTag();
            if (lTag.m_cColor.equals("YELLOW")) {
                m_cSelectedDate = lTag.m_cFormatDate;

                if (null != m_cSelectedView) {
                    EachDate lEachDay = (EachDate) m_cSelectedView.getTag();
                    if (lEachDay.m_cColor.equals("GREY")) {
                        m_cSelectedView.setTextColor(Color.LTGRAY);
                        /*m_cSelectedView.setEnabled(false);
                        m_cSelectedView.setClickable(false);
                        m_cSelectedView.setFocusable(false);
                        m_cSelectedView.setFocusableInTouchMode(false);*/
                    }
                    if (lEachDay.m_cColor.equals("WHITE")) {
                        m_cSelectedView.setTextColor(Color.LTGRAY);
                    }
                    if (lEachDay.m_cColor.equals("BLUE")) {
                        m_cSelectedView.setTextColor(Color.LTGRAY);
                    }
                    if (lEachDay.m_cColor.equals("YELLOW")) {
                        m_cSelectedView.setTextColor(Color.BLACK);
                    }
                    m_cSelectedView.setBackgroundResource(R.drawable.cal_tile_selector);
                }
                m_cSelectedView = (CalenderSqureButton) v;
                m_cSelectedView.setTextColor(Color.WHITE);
                m_cSelectedView.setBackgroundResource(R.drawable.selectdatebg);
//                m_cMrngGridSlots.setVisibility(View.VISIBLE);
                updateUIbyDate();
            } else {
                int xOffset = Integer.parseInt(((TextView) v).getText().toString());
                int yOffset = Integer.parseInt(((TextView) v).getText().toString());
//                EURemediesMacros.showCustomAlert(m_cContext, xOffset, yOffset);
//                Toast.makeText(m_cContext, "Appointment not available..", Toast.LENGTH_SHORT).show();
            }
        }

        private void printMonth(int mm, int yy) {
            // The number of days to leave blank at
            // the start of this month.
            int lTrailingSpaces = 0;
            int lDaysInPrevMonth = 0;
            int lPrevMonth = 0;
            int lPrevYear = 0;
            int lNextMonth = 0;
            int lNextYear = 0;

            int lCurrentMonth = mm;
            m_cDaysInMonth = getNumberOfDaysOfMonth(lCurrentMonth);

            // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
            GregorianCalendar cal = new GregorianCalendar(yy, lCurrentMonth, 1); //changing the value to 0 - starts day from
            // monday changing the value to 1 - starts day from sunday and so on..

            if (lCurrentMonth == 11) {
                lPrevMonth = lCurrentMonth - 1;
                lDaysInPrevMonth = getNumberOfDaysOfMonth(lPrevMonth);
                lNextMonth = 0;
                lPrevYear = yy;
                lNextYear = yy + 1;
            } else if (lCurrentMonth == 0) {
                lPrevMonth = 11;
                lPrevYear = yy - 1;
                lNextYear = yy;
                lDaysInPrevMonth = getNumberOfDaysOfMonth(lPrevMonth);
                lNextMonth = 1;
            } else {
                lPrevMonth = lCurrentMonth - 1;
                lNextMonth = lCurrentMonth + 1;
                lNextYear = yy;
                lPrevYear = yy;
                lDaysInPrevMonth = getNumberOfDaysOfMonth(lPrevMonth);
            }

            // Compute how much to leave before before the first day of the
            // month.
            // getDay() returns 0 for Sunday.
            int lCurrentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            lTrailingSpaces = lCurrentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
                ++m_cDaysInMonth;
            }

            // Trailing Month days
            EachDate lDay = null;
            for (int i = 0; i < lTrailingSpaces; i++) {
                lDay = new EachDate();
                int lMonth = lPrevMonth + 1;
                String lMonthSt = null;
                if (lMonth <= 9) {
                    lMonthSt = "0" + lMonth;
                } else {
                    lMonthSt = "" + lMonth;
                }
                lDay.m_cFormatDate = String.format("%d-%s-%d", lPrevYear, lMonthSt, (lDaysInPrevMonth - lTrailingSpaces + DAY_OFFSET) + i);
                String lDate = String.valueOf((lDaysInPrevMonth - lTrailingSpaces + DAY_OFFSET) + i) + "-" + getMonthAsString(lPrevMonth) + "-" + lPrevYear;
                lDay.m_cDate = lDate;
                lDay.m_cColor = "GREY";
                m_cObjList.add(lDay);
                /*m_cObjList.add(String.valueOf((lDaysInPrevMonth
						- lTrailingSpaces + DAY_OFFSET)
						+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(lPrevMonth)
						+ "-"
						+ lPrevYear);*/
            }
            Todaysis();
            for (int i = 1; i <= m_cDaysInMonth; i++) {
                lDay = new EachDate();
                int lMonth = lCurrentMonth + 1;
                String lMonthSt = null;
                if (lMonth <= 9) {
                    lMonthSt = "0" + lMonth;
                } else {
                    lMonthSt = "" + lMonth;
                }
                String lday = null;
                if (i <= 9) {
                    lday = "0" + i;
                } else {
                    lday = "" + i;
                }
                lDay.m_cFormatDate = String.format("%d-%s-%s", yy, lMonthSt, lday);
                String lDate = String.valueOf(i) + "-" + getMonthAsString(lCurrentMonth) + "-" + yy;

                lDay.m_cDate = lDate;
                if (i == m_cToday_Date && m_cMonth_Now == m_cMonth && m_cYear_Now == m_cYear) {
                    lDay.m_cColor = "BLUE";
                } else {
                    lDay.m_cColor = "WHITE";
                }

                /*if(null != m_cAbsents && m_cAbsents.containsKey(lDay.m_cFormatDate)) {
                    lDay.m_cColor = "YELLOW" ;
                }*/
                m_cObjList.add(lDay);
            }

            for (int i = 0; i < m_cObjList.size() % 7; i++) {
                lDay = new EachDate();
                int lMonth = lNextMonth + 1;
                String lMonthSt = null;
                if (lMonth <= 9) {
                    lMonthSt = "0" + lMonth;
                } else {
                    lMonthSt = "" + lMonth;
                }
                String lDate = String.valueOf(i + 1) + "-" + getMonthAsString(lNextMonth) + "-" + lNextYear;
                lDay.m_cFormatDate = String.format("%d-%s-%d", lNextYear, lMonthSt, i + 1);
                lDay.m_cDate = lDate;
                lDay.m_cColor = "GREY";
                m_cObjList.add(lDay);
            }

        }

        private String getMonthAsString(int i) {
            return m_cMonths[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return m_cDaysOfMonth[i];
        }
    }

    public class EachDate {
        public String m_cDate;
        public String m_cColor;
        public String m_cFormatDate;
    }


}


