console.log("JavaScript is Working!!!");

function toggleSidebar() {
               
          if($(".sidebar").is(":visible")) {
 
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
        $(".barrr").css("display", "none");

       }
        else {

        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
        $(".barrr").css("display", "block");
        }
};